package io.github.mattlavallee.checksandbalances.ui.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.mattlavallee.checksandbalances.R
import io.github.mattlavallee.checksandbalances.core.Constants
import io.github.mattlavallee.checksandbalances.ui.account.AccountFragment
import io.github.mattlavallee.checksandbalances.ui.forms.AccountBottomSheet
import io.github.mattlavallee.checksandbalances.ui.forms.FeedbackBottomSheet
import io.github.mattlavallee.checksandbalances.ui.forms.ManageTagBottomSheet
import io.github.mattlavallee.checksandbalances.ui.forms.TransactionBottomSheet
import io.github.mattlavallee.checksandbalances.ui.tags.ManageTagsFragment
import io.github.mattlavallee.checksandbalances.ui.transactions.TransactionFragment

class FormDispatcher {
    companion object{
        fun launch(supportFragmentManager: FragmentManager, formType: String, bundle: Bundle?) {
            var bottomSheetDialog: BottomSheetDialogFragment? = null
            when(formType) {
                Constants.accountFormTag -> {
                    bottomSheetDialog = supportFragmentManager.findFragmentByTag(formType) as AccountBottomSheet?
                        ?: AccountBottomSheet()
                }
                Constants.transactionFormTag -> {
                    bottomSheetDialog = supportFragmentManager.findFragmentByTag(formType) as TransactionBottomSheet?
                        ?: TransactionBottomSheet()
                }
                Constants.accountViewTag -> {
                    return launchFragment(supportFragmentManager, formType, bundle)
                }
                Constants.transactionViewTag -> {
                    return launchFragment(supportFragmentManager, formType, bundle)
                }
                Constants.manageTagsViewTag -> {
                    return launchFragment(supportFragmentManager, formType, bundle)
                }
                Constants.manageTagsFormTag -> {
                    bottomSheetDialog = supportFragmentManager.findFragmentByTag(formType) as ManageTagBottomSheet?
                        ?: ManageTagBottomSheet()
                }
                Constants.feedbackTag -> {
                    bottomSheetDialog = supportFragmentManager.findFragmentByTag(formType) as FeedbackBottomSheet?
                        ?: FeedbackBottomSheet()
                }
            }

            bottomSheetDialog?.arguments = bundle
            bottomSheetDialog?.show(supportFragmentManager, formType)
        }

        private fun launchFragment(supportFragmentManager: FragmentManager, formType: String, bundle: Bundle?) {
            var fragment: Fragment? = supportFragmentManager.findFragmentByTag(formType)
            if (fragment == null) {
                fragment = if (formType == Constants.accountViewTag) AccountFragment() else if (formType == Constants.transactionViewTag) TransactionFragment() else ManageTagsFragment()
                fragment.arguments = bundle;
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, fragment, formType)
                .addToBackStack(formType)
                .commit()
        }
    }
}