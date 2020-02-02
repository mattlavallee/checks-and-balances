package io.github.mattlavallee.checksandbalances.ui.navigation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView
import io.github.mattlavallee.checksandbalances.R
import io.github.mattlavallee.checksandbalances.core.Constants
import io.github.mattlavallee.checksandbalances.ui.forms.AccountBottomSheet

class NavigationBottomSheet: BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val homeView = inflater.inflate(R.layout.layout_home_bottomsheet, container, false)

        val navigationView: NavigationView = homeView.findViewById(R.id.home_navigation_view)
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home_create_account_menu_button -> {
                    val accountForm: AccountBottomSheet =
                        activity!!.supportFragmentManager.findFragmentByTag(Constants.accountFormTag) as AccountBottomSheet?
                            ?: AccountBottomSheet()
                    accountForm.show(activity!!.supportFragmentManager, Constants.accountFormTag)
                }
                R.id.home_create_transaction_menu_button -> toast(activity!!.applicationContext, "Create Transaction")
                R.id.home_help_feedback_menu_button -> toast(activity!!.applicationContext, "Help me Rhonda")
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
