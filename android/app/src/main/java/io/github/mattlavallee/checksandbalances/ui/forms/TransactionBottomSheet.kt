package io.github.mattlavallee.checksandbalances.ui.forms

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputLayout
import io.github.mattlavallee.checksandbalances.R
import io.github.mattlavallee.checksandbalances.core.models.Account
import io.github.mattlavallee.checksandbalances.databinding.LayoutTransactionFormBinding
import io.github.mattlavallee.checksandbalances.ui.account.AccountViewModel
import io.github.mattlavallee.checksandbalances.ui.transactions.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.collections.ArrayList

class TransactionBottomSheet: BottomSheetDialogFragment() {
    private val transactionViewModel: TransactionViewModel by activityViewModels()
    private val accountViewModel: AccountViewModel by activityViewModels()
    private val accounts: ArrayList<Account> = ArrayList()
    private var transactionCalendar: Calendar = Calendar.getInstance()
    private lateinit var binding: LayoutTransactionFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CABBottomSheet)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val transactionView = inflater.inflate(R.layout.layout_transaction_form, container, false)
        binding = LayoutTransactionFormBinding.bind(transactionView)

        val transactionId = arguments?.getInt("transactionId")
        binding.editTransactionTitle.requestFocus()
        val inputMethodManager: InputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
        this.initializeDateTimePicker()

        accountViewModel.getAllAccounts().observe(viewLifecycleOwner, Observer {itList ->
            val accountNames: ArrayList<String> = ArrayList()
            itList.mapTo(accounts) { Account(it.id, it.name, it.description?: "", it.startingBalance, it.isActive) }
                .sortBy { it.name }

            accounts.mapTo(accountNames) { it.name }
            binding.editTransactionAccountId.adapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, accountNames)
            if (transactionId == null) {
                binding.editTransactionAccountId.setSelection(0, true)
            }
        })

        binding.editTransactionTitle.addTextChangedListener {
            this.checkForValidField(it.toString(), binding.editTransactionTitleWrapper, "Title",false)
        }
        binding.editTransactionAmount.addTextChangedListener{
            this.checkForValidField(it.toString(), binding.editTransactionAmountWrapper, "Amount", false)
            if (it.toString().startsWith("-")) {
                binding.editTransactionAmountDeduction.isChecked = true
                it?.delete(0,1)
            }
        }
        binding.editTransactionSaveButton.setOnClickListener {
            val accountId = accounts[binding.editTransactionAccountId.selectedItemPosition].id
            val title = binding.editTransactionTitle.text.toString()
            val description = binding.editTransactionDescription.text.toString()
            val titleError = this.checkForValidField(title, binding.editTransactionTitleWrapper, "Title", true)
            val amountError = this.checkForValidField(binding.editTransactionAmount.text.toString(), binding.editTransactionAmountWrapper, "Amount", true)

            if (titleError || amountError) {
                return@setOnClickListener
            }

            var amount = binding.editTransactionAmount.text.toString().toDouble()
            if (binding.editTransactionAmountDeduction.isChecked) {
                amount *= -1
            }

            if (transactionId == null) {
                transactionViewModel.save(
                    -1,
                    accountId,
                    title,
                    amount,
                    description,
                    transactionCalendar.time.time
                )
            } else {
                transactionViewModel.update(
                    transactionId,
                    accountId,
                    title,
                    amount,
                    description,
                    transactionCalendar.time.time
                )
            }
            inputMethodManager.hideSoftInputFromWindow(transactionView.windowToken, 0)
            super.dismiss()
        }

        if (transactionId != null) {
            this.populateForm(transactionId)
        }

        return transactionView
    }

    private fun initializeDateTimePicker() {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy")
        transactionCalendar = Calendar.getInstance()
        val dateListener: DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            transactionCalendar.set(Calendar.YEAR, year)
            transactionCalendar.set(Calendar.MONTH, monthOfYear)
            transactionCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            binding.editTransactionDatetime.setText(dateFormat.format(transactionCalendar.time))
        }

        binding.editTransactionDatetime.setText(dateFormat.format(transactionCalendar.time))
        binding.editTransactionDatetime.setOnClickListener {
            val dialog = DatePickerDialog(
                requireContext(),
                R.style.CABDialog,
                dateListener,
                transactionCalendar.get(Calendar.YEAR),
                transactionCalendar.get(Calendar.MONTH),
                transactionCalendar.get(Calendar.DAY_OF_MONTH)
            )
            dialog.show()
            dialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        }
    }

    private fun populateForm(transactionId: Int) {}

    private fun checkForValidField(name: String, field: TextInputLayout, fieldName: String, checkForError: Boolean): Boolean {
        val hasError = if (fieldName == "Title") {
            name.isEmpty()
        } else {
            name.toDoubleOrNull() == null
        }

        if (hasError && checkForError) {
            field.error = "$fieldName is Required"
            field.isErrorEnabled = true;
            return true
        } else if (!hasError) {
            field.error = null
            field.isErrorEnabled = false
        }
        return false
    }
}
