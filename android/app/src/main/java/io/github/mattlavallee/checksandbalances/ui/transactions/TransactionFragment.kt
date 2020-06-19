package io.github.mattlavallee.checksandbalances.ui.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import io.github.mattlavallee.checksandbalances.R
import io.github.mattlavallee.checksandbalances.databinding.FragmentTransactionBinding

class TransactionFragment: Fragment() {
    private val transactionViewModel: TransactionViewModel by activityViewModels()
    private lateinit var binding: FragmentTransactionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_transaction, container, false)
        binding = FragmentTransactionBinding.bind(root)

        val accountId = arguments?.getInt("accountId")
        transactionViewModel.getAccount(accountId!!).observe(viewLifecycleOwner, Observer {
            binding.transactionAcountName.text = it.name
        })

        return root
    }
}
