package io.github.mattlavallee.checksandbalances.ui.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.mattlavallee.checksandbalances.R
import io.github.mattlavallee.checksandbalances.core.Constants
import io.github.mattlavallee.checksandbalances.databinding.LayoutHomeBottomsheetBinding
import io.github.mattlavallee.checksandbalances.ui.transactions.TransactionFragment

class NavigationBottomSheet: BottomSheetDialogFragment() {
    private lateinit var binding: LayoutHomeBottomsheetBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val homeView = inflater.inflate(R.layout.layout_home_bottomsheet, container, false)
        binding = LayoutHomeBottomsheetBinding.bind(homeView)

        val transFragment = this.setArchiveTransactionState(requireActivity().supportFragmentManager)

        binding.homeNavigationView.setNavigationItemSelectedListener {
            val fragmentManager = requireActivity().supportFragmentManager
            when (it.itemId) {
                R.id.home_create_account_menu_button -> FormDispatcher.launch(fragmentManager, Constants.accountFormTag, null)
                R.id.home_create_transaction_menu_button -> FormDispatcher.launch(fragmentManager, Constants.transactionFormTag, null)
                R.id.home_delete_all_transactions_menu_button -> transFragment?.deleteTransactions()
                R.id.home_help_feedback_menu_button -> FormDispatcher.launch(fragmentManager, Constants.feedbackTag, null)
            }

            super.dismiss()
            true
        }

        return homeView
    }

    private fun setArchiveTransactionState(fragManager: FragmentManager): TransactionFragment? {
        val stackCount = fragManager.backStackEntryCount
        val currStackEntry = if (stackCount > 0) fragManager.getBackStackEntryAt(stackCount - 1) else null

        val isTransactionFragment = currStackEntry?.name == Constants.transactionViewTag
        val deleteTransOpt = binding.homeNavigationView.menu.getItem(2)
        deleteTransOpt.isEnabled = isTransactionFragment

        val activeFragment = fragManager.findFragmentByTag(currStackEntry?.name)
        return if (activeFragment == null) null else activeFragment as TransactionFragment
    }
}
