package io.github.mattlavallee.checksandbalances.ui.forms

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import io.github.mattlavallee.checksandbalances.R
import io.github.mattlavallee.checksandbalances.core.models.Account
import io.github.mattlavallee.checksandbalances.ui.account.AccountViewModel

class AccountBottomSheet: BottomSheetDialogFragment() {
    private lateinit var accountViewModel: AccountViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel::class.java)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CABBottomSheet)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val accountView = inflater.inflate(R.layout.layout_account_form, container, false)

        val accountNameWrapper: TextInputLayout = accountView.findViewById(R.id.edit_account_name_wrapper)
        val accountNameEditText: TextInputEditText = accountView.findViewById(R.id.edit_account_name)
        val accountDescriptionEditText: TextInputEditText = accountView.findViewById(R.id.edit_account_description)
        val accountInitialBalance: TextInputEditText = accountView.findViewById(R.id.edit_account_initial_balance)
        val saveButton: MaterialButton = accountView.findViewById(R.id.edit_account_save_button)

        accountNameEditText.requestFocus()
        accountNameEditText.addTextChangedListener {
            this.checkForValidAccountName(it.toString(), accountNameWrapper, false)
        }

        val inputMethodManager: InputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)

        saveButton.setOnClickListener {
            val name = accountNameEditText.text.toString()
            val description = accountDescriptionEditText.text.toString()
            var balance = accountInitialBalance.text.toString().toDoubleOrNull()

            val hasError = this.checkForValidAccountName(name, accountNameWrapper, true)
            if(!hasError) {
                inputMethodManager.hideSoftInputFromWindow(accountView.windowToken, 0)
                if (balance == null) {
                    balance = 0.0
                }
                this.accountViewModel.save(Account(-1, name, description, balance, true))
            }
        }

        return accountView
    }

    private fun checkForValidAccountName(name: String, field: TextInputLayout, checkForError: Boolean): Boolean {
        if (name.isNotEmpty()) {
            field.error = null
            field.isErrorEnabled = false
            return false
        } else if(checkForError) {
            field.isErrorEnabled = true
            field.error = "Account Name is Required"
            return true
        }
        return false
    }
}
