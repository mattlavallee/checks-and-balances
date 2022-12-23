package io.github.mattlavallee.checksandbalances.ui.navigation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.mattlavallee.checksandbalances.R
import io.github.mattlavallee.checksandbalances.core.Constants
import io.github.mattlavallee.checksandbalances.databinding.LayoutHomeBottomsheetBinding

class NavigationBottomSheet: BottomSheetDialogFragment() {
    private lateinit var binding: LayoutHomeBottomsheetBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val homeView = inflater.inflate(R.layout.layout_home_bottomsheet, container, false)
        binding = LayoutHomeBottomsheetBinding.bind(homeView)

        binding.homeNavigationView.setNavigationItemSelectedListener {
            val fragmentManager = requireActivity().supportFragmentManager
            when (it.itemId) {
                R.id.home_create_account_menu_button -> FormDispatcher.launch(fragmentManager, Constants.accountFormTag, null)
                R.id.home_create_transaction_menu_button -> FormDispatcher.launch(fragmentManager, Constants.transactionFormTag, null)
                R.id.home_help_feedback_menu_button -> FormDispatcher.launch(fragmentManager, Constants.feedbackTag, null)
            }

            super.dismiss()
            true
        }

        return homeView
    }

    private fun toast(context: Context, message: String) {
        return Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
