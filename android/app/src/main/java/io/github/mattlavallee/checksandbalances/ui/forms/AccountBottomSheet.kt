package io.github.mattlavallee.checksandbalances.ui.forms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.mattlavallee.checksandbalances.R

class AccountBottomSheet: BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val accountView = inflater.inflate(R.layout.layout_account_form, container, false)

        return accountView
    }
}
