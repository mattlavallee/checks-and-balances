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
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import io.github.mattlavallee.checksandbalances.R
import io.github.mattlavallee.checksandbalances.core.models.Account
import io.github.mattlavallee.checksandbalances.databinding.LayoutAccountFormBinding
import io.github.mattlavallee.checksandbalances.ui.account.AccountViewModel

class AccountBottomSheet: BottomSheetDialogFragment() {
    private val accountViewModel: AccountViewModel by activityViewModels()
    private lateinit var binding: LayoutAccountFormBinding

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
        binding = LayoutAccountFormBinding.bind(accountView)

        binding.editAccountName.requestFocus()
        binding.editAccountName.addTextChangedListener {
            this.checkForValidAccountName(it.toString(), binding.editAccountNameWrapper, false)
        }

        val inputMethodManager: InputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)

        binding.editAccountSaveButton.setOnClickListener {
            val name = binding.editAccountName.text.toString()
            val description = binding.editAccountDescription.text.toString()
            var balance = binding.editAccountInitialBalance.text.toString().toDoubleOrNull()

            val hasError = this.checkForValidAccountName(name, binding.editAccountNameWrapper, true)
            if(!hasError) {
                inputMethodManager.hideSoftInputFromWindow(accountView.windowToken, 0)
                if (balance == null) {
                    balance = 0.0
                }
                this.accountViewModel.save(Account(0, name, description, balance, true))
                super.dismiss()
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
