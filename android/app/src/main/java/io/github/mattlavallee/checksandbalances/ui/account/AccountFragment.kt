package io.github.mattlavallee.checksandbalances.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.mattlavallee.checksandbalances.R
import io.github.mattlavallee.checksandbalances.core.Constants
import io.github.mattlavallee.checksandbalances.core.adapters.AccountAdapter
import io.github.mattlavallee.checksandbalances.core.models.Account
import io.github.mattlavallee.checksandbalances.databinding.FragmentAccountBinding
import io.github.mattlavallee.checksandbalances.ui.navigation.FormDispatcher

class AccountFragment: Fragment() {
    private val accountViewModel: AccountViewModel by activityViewModels()
    private lateinit var binding: FragmentAccountBinding
    private var accountAdapter: AccountAdapter = AccountAdapter(::onEditAccount, ::onDeleteAccount)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_account, container, false)
        binding = FragmentAccountBinding.bind(root)

        accountViewModel.getAllAccounts().observe(viewLifecycleOwner, Observer {itList ->
            var accounts: ArrayList<Account> = ArrayList()
            itList.mapTo(accounts) {Account(it.id, it.name, it.description?: "", it.startingBalance, it.isActive)}
            accountAdapter.setData(accounts)
        })

        binding.accountRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.accountRecyclerView.setHasFixedSize(true)
        binding.accountRecyclerView.adapter = accountAdapter

        return root
    }

    private fun onEditAccount(accountId: Int): Unit {
        val bundle = Bundle()
        bundle.putInt("accountId", accountId)

        FormDispatcher.launch(activity!!.supportFragmentManager, Constants.accountFormTag, bundle)
    }

    private fun onDeleteAccount(accountId: Int): Unit {
        accountViewModel.delete(accountId)
    }
}
