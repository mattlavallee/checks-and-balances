package io.github.mattlavallee.checksandbalances.ui.transactions

import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.mattlavallee.checksandbalances.R
import io.github.mattlavallee.checksandbalances.core.Constants
import io.github.mattlavallee.checksandbalances.core.FormatUtils
import io.github.mattlavallee.checksandbalances.core.Preferences
import io.github.mattlavallee.checksandbalances.core.adapters.TransactionAdapter
import io.github.mattlavallee.checksandbalances.database.entities.TransactionWithTags
import io.github.mattlavallee.checksandbalances.databinding.FragmentTransactionBinding
import io.github.mattlavallee.checksandbalances.ui.account.AccountViewModel
import io.github.mattlavallee.checksandbalances.ui.navigation.FormDispatcher
import kotlin.collections.ArrayList

class TransactionFragment: Fragment() {
    private val transactionViewModel: TransactionViewModel by activityViewModels()
    private val accountViewModel: AccountViewModel by activityViewModels()
    private lateinit var binding: FragmentTransactionBinding
    private lateinit var preferences: Preferences
    private var accountId: Int? = null
    private lateinit var transactionAdapter: TransactionAdapter
    private val listener: SharedPreferences.OnSharedPreferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener {_, key ->
        if (key == "transactionSort") {
            transactionAdapter.setSortField(preferences.getTransactionSortField())
        } else if (key == "positiveColor" || key == "negativeColor") {
            transactionAdapter.notifyItemRangeChanged(0, transactionAdapter.itemCount)
        }
    }

    private var accountName: String = ""
    private var totalBalance: Double = 0.0
    private var accountStartingBalance: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_transaction, container, false)
        binding = FragmentTransactionBinding.bind(root)
        preferences = Preferences(requireActivity())
        transactionAdapter = TransactionAdapter(preferences, ::onEditTransaction, ::onArchiveTransaction, ::onDeleteTransaction)

        accountId = arguments?.getInt("accountId")
        accountViewModel.activeAccountId.value = accountId

        val width = Resources.getSystem().displayMetrics.widthPixels
        val layoutParams = binding.transactionSplashLogo.layoutParams
        layoutParams.width = (width / 2)
        layoutParams.height = (width / 2)
        binding.transactionSplashLogo.layoutParams = layoutParams

        transactionViewModel.getAccount(accountId!!).observe(viewLifecycleOwner) {
            this.accountName = it.name
            this.accountStartingBalance = it.startingBalance
            binding.transactionAccountName.text = getString(R.string.account_and_balance, this.accountName, FormatUtils.currencyFormat().format(this.accountStartingBalance + this.totalBalance))
        }
        transactionViewModel.getTransactionsForAccount(accountId!!).observe(viewLifecycleOwner) { itList ->
            val transactions: ArrayList<TransactionWithTags> = ArrayList()
            itList.mapTo(transactions) {
                TransactionWithTags(it.transaction, it.tags)
            }
            transactionAdapter.setData(transactions)
            transactionAdapter.setSortField(preferences.getTransactionSortField())

            this.totalBalance = 0.0
            transactions.forEach { this.totalBalance += it.transaction.amount }

            binding.transactionAccountName.text = getString(R.string.account_and_balance, this.accountName, FormatUtils.currencyFormat().format(this.accountStartingBalance + this.totalBalance))

            val emptyMessageVisibility = if (itList.isNotEmpty()) View.GONE else View.VISIBLE
            binding.transactionEmptyContainer.visibility = emptyMessageVisibility
        }

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

    fun deleteTransactions() {
        transactionViewModel.archiveAll(accountId!!)
    }

    private fun onArchiveTransaction(transactionId: Int) {
        transactionViewModel.archive(transactionId)
    }

    private fun onDeleteTransaction(transactionId: Int) {
        transactionViewModel.delete(transactionId)
    }

    override fun onResume() {
        super.onResume()
        preferences.preferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onPause() {
        super.onPause()
        preferences.preferences.unregisterOnSharedPreferenceChangeListener(listener)
    }
}
