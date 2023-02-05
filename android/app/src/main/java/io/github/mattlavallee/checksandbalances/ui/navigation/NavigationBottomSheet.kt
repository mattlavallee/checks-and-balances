package io.github.mattlavallee.checksandbalances.ui.navigation

import android.os.Bundle
import android.view.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.mattlavallee.checksandbalances.R
import io.github.mattlavallee.checksandbalances.core.Constants
import io.github.mattlavallee.checksandbalances.database.entities.Account
import io.github.mattlavallee.checksandbalances.databinding.LayoutHomeBottomsheetBinding
import io.github.mattlavallee.checksandbalances.ui.account.AccountViewModel

class NavigationBottomSheet: BottomSheetDialogFragment() {
    private lateinit var binding: LayoutHomeBottomsheetBinding
    private val accountViewModel: AccountViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val homeView = inflater.inflate(R.layout.layout_home_bottomsheet, container, false)
        binding = LayoutHomeBottomsheetBinding.bind(homeView)

        accountViewModel.getAllAccounts().observe(viewLifecycleOwner, Observer {
            val addTransactionItem = binding.homeNavigationView.menu.getItem(1)
            addTransactionItem.isVisible = it.isNotEmpty()
        })

        binding.homeNavigationView.setNavigationItemSelectedListener {
            val fragmentManager = requireActivity().supportFragmentManager
            when (it.itemId) {
                R.id.home_create_account_menu_button -> FormDispatcher.launch(fragmentManager, Constants.accountFormTag, null)
                R.id.home_create_transaction_menu_button -> FormDispatcher.launch(fragmentManager, Constants.transactionFormTag, null)
                R.id.home_account_overview_menu_button -> FormDispatcher.launch(fragmentManager, Constants.accountViewTag, null)
                R.id.home_manage_tags_menu_button -> FormDispatcher.launch(fragmentManager, Constants.manageTagsViewTag, null)
                R.id.home_help_feedback_menu_button -> FormDispatcher.launch(fragmentManager, Constants.feedbackTag, null)
            }

            super.dismiss()
            true
        }

        return homeView
    }
}
