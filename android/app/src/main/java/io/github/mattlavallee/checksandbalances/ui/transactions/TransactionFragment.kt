package io.github.mattlavallee.checksandbalances.ui.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.mattlavallee.checksandbalances.R
import io.github.mattlavallee.checksandbalances.core.Constants
import io.github.mattlavallee.checksandbalances.core.adapters.TransactionAdapter
import io.github.mattlavallee.checksandbalances.database.entities.Transaction
import io.github.mattlavallee.checksandbalances.databinding.FragmentTransactionBinding
import io.github.mattlavallee.checksandbalances.ui.account.AccountViewModel
import io.github.mattlavallee.checksandbalances.ui.navigation.FormDispatcher

class TransactionFragment: Fragment() {
    private val transactionViewModel: TransactionViewModel by activityViewModels()
    private val accountViewModel: AccountViewModel by activityViewModels()
    private lateinit var binding: FragmentTransactionBinding
    private var accountId: Int? = null
    private var transactionAdapter: TransactionAdapter = TransactionAdapter(::onEditTransaction, ::onArchiveTransaction)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_transaction, container, false)
        binding = FragmentTransactionBinding.bind(root)

        accountId = arguments?.getInt("accountId")
        accountViewModel.activeAccountId.value = accountId
        transactionViewModel.getAccount(accountId!!).observe(viewLifecycleOwner, Observer {
            binding.transactionAccountName.text = it.name
        })
        transactionViewModel.getTransactionsForAccount(accountId!!).observe(viewLifecycleOwner, Observer { itList ->
            val transactions: ArrayList<Transaction> = ArrayList()
            itList.mapTo(transactions) {
                Transaction(it.id, it.accountId, it.title, it.amount, it.description, it.dateTimeModified, it.isActive)
            }
            transactionAdapter.setData(transactions)
        })

        binding.transactionRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.transactionRecyclerView.setHasFixedSize(true)
        binding.transactionRecyclerView.adapter = transactionAdapter

        return root
    }

    private fun onEditTransaction(transactionId: Int) {
        val bundle = Bundle()
        bundle.putInt("transactionId", transactionId)
        bundle.putInt("accountId", accountId!!)

        FormDispatcher.launch(requireActivity().supportFragmentManager, Constants.transactionFormTag, bundle)
    }

    private fun onArchiveTransaction(transactionId: Int) {
        transactionViewModel.archive(transactionId)
    }
}
