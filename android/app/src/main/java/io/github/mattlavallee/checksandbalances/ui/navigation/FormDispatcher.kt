package io.github.mattlavallee.checksandbalances.ui.navigation

import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.mattlavallee.checksandbalances.core.Constants
import io.github.mattlavallee.checksandbalances.ui.forms.AccountBottomSheet

class FormDispatcher {
    companion object{
        fun launch(supportFragmentManager: FragmentManager, formType: String) {
            var bottomSheetDialog: BottomSheetDialogFragment? = null
            if (formType == Constants.accountFormTag) {
                bottomSheetDialog = supportFragmentManager.findFragmentByTag(formType) as AccountBottomSheet?
                    ?: AccountBottomSheet()
            }

            bottomSheetDialog?.show(supportFragmentManager, formType)
        }
    }
}