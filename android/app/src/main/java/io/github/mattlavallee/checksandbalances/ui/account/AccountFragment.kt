package io.github.mattlavallee.checksandbalances.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.mattlavallee.checksandbalances.R
import io.github.mattlavallee.checksandbalances.core.adapters.AccountAdapter
import io.github.mattlavallee.checksandbalances.core.models.Account

class AccountFragment: Fragment() {
    private lateinit var accountViewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_account, container, false)

//        accountViewModel.text.observe(this, Observer {
//            textView.text = it
//        })

        val recyclerView: RecyclerView = root.findViewById(R.id.account_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.setHasFixedSize(true)

        val accounts = ArrayList<Account>()
        accounts.add(Account(1, "Bank of America", 0.0, true))
        accounts.add(Account(2, "Capital One", 0.0, true))
        accounts.add(Account(3, "American Express", 0.0, true))
        accounts.add(Account(4, "Discover", 0.0, true))
        accounts.add(Account(5, "Macy's", 0.0, true))
        accounts.add(Account(6, "Visa", 0.0, true))
        accounts.add(Account(7, "Mastercard", 0.0, true))
        accounts.add(Account(8, "Amazon Card", 0.0, true))
        accounts.add(Account(9, "Party Planning", 0.0, true))
        accounts.add(Account(10, "Gifts", 0.0, true))
        accounts.add(Account(11, "IOU's", 0.0, true))

        val accountAdapter = AccountAdapter(accounts)
        recyclerView.adapter = accountAdapter

        return root
    }
}
