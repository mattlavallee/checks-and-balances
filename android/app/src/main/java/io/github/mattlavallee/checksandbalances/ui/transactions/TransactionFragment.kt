package io.github.mattlavallee.checksandbalances.ui.transactions

import android.content.SharedPreferences
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
import io.github.mattlavallee.checksandbalances.core.Preferences
import io.github.mattlavallee.checksandbalances.core.adapters.TransactionAdapter
import io.github.mattlavallee.checksandbalances.database.entities.TransactionWithTags
import io.github.mattlavallee.checksandbalances.databinding.FragmentTransactionBinding
import io.github.mattlavallee.checksandbalances.ui.account.AccountViewModel
import io.github.mattlavallee.checksandbalances.ui.navigation.FormDispatcher
import java.text.NumberFormat
import java.util.*
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
            transactionAdapter.notifyDataSetChanged()
        }
    }

    private var accountName: String = ""
    private var totalBalance: Double = 0.0
    private val currencyFormat = NumberFormat.getCurrencyInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_transaction, container, false)
        binding = FragmentTransactionBinding.bind(root)
        preferences = Preferences(requireActivity())
        transactionAdapter = TransactionAdapter(preferences, ::onEditTransaction, ::onArchiveTransaction)
        currencyFormat.maximumFractionDigits = 2
        currencyFormat.currency = Currency.getInstance("USD")

        accountId = arguments?.getInt("accountId")
        accountViewModel.activeAccountId.value = accountId
        transactionViewModel.getAccount(accountId!!).observe(viewLifecycleOwner, Observer {
            this.accountName = it.name
            binding.transactionAccountName.text = getString(R.string.account_and_balance, this.accountName, currencyFormat.format(this.totalBalance))
        })
        transactionViewModel.getTransactionsForAccount(accountId!!).observe(viewLifecycleOwner, Observer { itList ->
            val transactions: ArrayList<TransactionWithTags> = ArrayList()
            itList.mapTo(transactions) {
                TransactionWithTags(it.transaction, it.tags)
            }
            transactionAdapter.setData(transactions)
            transactionAdapter.setSortField(preferences.getTransactionSortField())

            this.totalBalance = 0.0
            transactions.forEach { this.totalBalance += it.transaction.amount }

            binding.transactionAccountName.text = getString(R.string.account_and_balance, this.accountName, currencyFormat.format(this.totalBalance))
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

    override fun onResume() {
        super.onResume()
        preferences.preferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onPause() {
        super.onPause()
        preferences.preferences.unregisterOnSharedPreferenceChangeListener(listener)
    }
}
