package io.github.mattlavallee.checksandbalances.ui.account

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
import io.github.mattlavallee.checksandbalances.core.Preferences
import io.github.mattlavallee.checksandbalances.core.Constants
import io.github.mattlavallee.checksandbalances.core.adapters.AccountAdapter
import io.github.mattlavallee.checksandbalances.database.entities.Account
import io.github.mattlavallee.checksandbalances.databinding.FragmentAccountBinding
import io.github.mattlavallee.checksandbalances.ui.navigation.FormDispatcher

class AccountFragment: Fragment() {
    private val accountViewModel: AccountViewModel by activityViewModels()
    private lateinit var binding: FragmentAccountBinding
    private lateinit var preferences: Preferences
    private lateinit var accountAdapter: AccountAdapter
    private val listener: SharedPreferences.OnSharedPreferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener{_, key ->
        if (key == "accountSort") {
            accountAdapter.setSortField(preferences.getAccountSortField())
        } else if (key == "positiveColor" || key == "negativeColor") {
            accountAdapter.notifyItemRangeChanged(0, accountAdapter.itemCount)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_account, container, false)
        binding = FragmentAccountBinding.bind(root)
        preferences = Preferences(requireActivity())
        accountAdapter = AccountAdapter(preferences, ::onEditAccount, ::onArchiveAccount, ::onDrillIntoAccount)

        val width = Resources.getSystem().displayMetrics.widthPixels
        val layoutParams = binding.accountSplashLogo.layoutParams
        layoutParams.width = (width / 2)
        layoutParams.height = (width / 2)
        binding.accountSplashLogo.layoutParams = layoutParams

        accountViewModel.getAllAccountsWithSum().observe(viewLifecycleOwner) {itList ->
            val accounts: ArrayList<Account> = ArrayList()
            itList.mapTo(accounts) { it ->
                val balance = it.starting_balance + it.sum
                Account(it.id, it.name, it.description?: "", balance, it.is_active)
            }
            accountAdapter.setData(accounts)
            accountAdapter.setSortField(preferences.getAccountSortField())

            val emptyMessageVisibility = if (itList.isNotEmpty()) View.GONE else View.VISIBLE
            binding.accountEmptyContainer.visibility = emptyMessageVisibility
        }

        binding.accountRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.accountRecyclerView.setHasFixedSize(true)
        binding.accountRecyclerView.adapter = accountAdapter

        return root
    }

    private fun onEditAccount(accountId: Int) {
        val bundle = Bundle()
        bundle.putInt("accountId", accountId)

        FormDispatcher.launch(requireActivity().supportFragmentManager, Constants.accountFormTag, bundle)
    }

    private fun onArchiveAccount(accountId: Int) {
        accountViewModel.archive(accountId)
    }

    private fun onDrillIntoAccount(accountId: Int) {
        val bundle = Bundle()
        bundle.putInt("accountId", accountId)

        FormDispatcher.launch(requireActivity().supportFragmentManager, Constants.transactionViewTag, bundle)
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
