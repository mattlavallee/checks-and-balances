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
import io.github.mattlavallee.checksandbalances.core.adapters.AccountAdapter
import io.github.mattlavallee.checksandbalances.core.models.Account

class AccountFragment: Fragment() {
    private val accountViewModel: AccountViewModel by activityViewModels()
    private var accountAdapter: AccountAdapter = AccountAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_account, container, false)

        accountViewModel.getAllAccounts().observe(this, Observer {itList ->
            var accounts: ArrayList<Account> = ArrayList()
            itList.mapTo(accounts) {Account(it.id, it.name, it.description?: "", it.startingBalance, it.isActive)}
            accountAdapter.setData(accounts)
        })

        val recyclerView: RecyclerView = root.findViewById(R.id.account_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = accountAdapter

        return root
    }
}
