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
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputLayout
import io.github.mattlavallee.checksandbalances.R
import io.github.mattlavallee.checksandbalances.database.entities.Account
import io.github.mattlavallee.checksandbalances.database.entities.Tag
import io.github.mattlavallee.checksandbalances.databinding.LayoutTransactionFormBinding
import io.github.mattlavallee.checksandbalances.ui.account.AccountViewModel
import io.github.mattlavallee.checksandbalances.ui.tags.TagViewModel
import io.github.mattlavallee.checksandbalances.ui.transactions.TransactionViewModel
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TransactionBottomSheet: BottomSheetDialogFragment() {
    private val transactionViewModel: TransactionViewModel by activityViewModels()
    private val accountViewModel: AccountViewModel by activityViewModels()
    private val tagViewModel: TagViewModel by activityViewModels()
    private val accounts: ArrayList<Account> = ArrayList()
    private val tags: ArrayList<Tag> = ArrayList()
    private val currentTransactionTags: ArrayList<Tag> = ArrayList()
    private var transactionCalendar: Calendar = Calendar.getInstance()
    private lateinit var binding: LayoutTransactionFormBinding
    private var accountId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CABBottomSheet)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val transactionView = inflater.inflate(R.layout.layout_transaction_form, container, false)
        binding = LayoutTransactionFormBinding.bind(transactionView)

        var transactionId = arguments?.getInt("transactionId")
        if (transactionId != null && transactionId < 0) {
            transactionId = null
        }
        accountId = arguments?.getInt("accountId")

        binding.editTransactionTitle.requestFocus()
        val inputMethodManager: InputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(binding.editTransactionTitle, 0)
        this.initializeDateTimePicker(null)

        accountViewModel.getAllAccounts().observe(viewLifecycleOwner, Observer {itList ->
            val accountNames: ArrayList<String> = ArrayList()
            itList.mapTo(accounts) { Account(it.id, it.name, it.description?: "", it.startingBalance, it.isActive) }
                .sortBy { it.name }

            accounts.mapTo(accountNames) { it.name }
            binding.editTransactionAccountId.adapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, accountNames)
            var index = 0
            if (accountId != null) {
                index = accounts.indexOfFirst { act -> act.id == accountId }
            }
            binding.editTransactionAccountId.setSelection(index, true)
        })

        tagViewModel.getAllTags().observe(viewLifecycleOwner, Observer {itList ->
            itList.mapTo(tags) { Tag(it.tagId, it.name, it.isActive) }.sortBy {it.name}
            val tagNames: ArrayList<String> = ArrayList()
            tags.mapTo(tagNames){it.name}
            binding.editTransactionTagAutocomplete.setAdapter(
                ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, tagNames)
            )
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

        binding.editTransactionTagAutocomplete.doAfterTextChanged { editable ->
            var tagName = editable.toString()
            if (tagName.indexOf(",") >= 0) {
                tagName = tagName.substringBefore(",")
                this.addChip(Tag(0, tagName, true))
                editable?.clear()
            }
        }

        binding.editTransactionTagAutocomplete.setOnItemClickListener { adapterView, _, position, _ ->
            val selectedItem = adapterView.getItemAtPosition(position).toString()
            val tag = this.tags.find { tag -> tag.name == selectedItem }
            if (tag != null) {
                this.addChip(tag)
                binding.editTransactionTagAutocomplete.text.clear()
            }
        }

        binding.editTransactionSaveButton.setOnClickListener {
            val accountId = accounts[binding.editTransactionAccountId.selectedItemPosition].id
            val title = binding.editTransactionTitle.text.toString()
            val description = binding.editTransactionDescription.text.toString()
            val titleError = this.checkForValidField(title, binding.editTransactionTitleWrapper, "Title", true)
            val amountError = this.checkForValidField(binding.editTransactionAmount.text.toString(), binding.editTransactionAmountWrapper, "Amount", true)

            val totalChips = binding.editTransactionSelectedTagGroups.childCount
            var tagsForTransaction = ArrayList<Tag>()
            var chipIndex = 0
            while(chipIndex < totalChips) {
                val currChip: Chip = binding.editTransactionSelectedTagGroups.getChildAt(chipIndex) as Chip
                tagsForTransaction.add(Tag(currChip.id, currChip.text.toString(), true))
                chipIndex++
            }

            if (titleError || amountError) {
                return@setOnClickListener
            }

            var amount = binding.editTransactionAmount.text.toString().toDouble()
            if (binding.editTransactionAmountDeduction.isChecked) {
                amount *= -1
            }

            if (transactionId == null) {
                transactionViewModel.save(
                    0,
                    accountId,
                    title,
                    amount,
                    description,
                    transactionCalendar.time.time,
                    tagsForTransaction
                )
            } else {
                val tagsToDelete = this.currentTransactionTags.filter {
                    tagsForTransaction.find { t-> t.tagId == it.tagId } == null
                }
                tagsForTransaction = ArrayList(tagsForTransaction.filter {
                    this.currentTransactionTags.find { cTag -> cTag.tagId == it.tagId } == null
                })
                transactionViewModel.update(
                    transactionId,
                    accountId,
                    title,
                    amount,
                    description,
                    transactionCalendar.time.time,
                    tagsForTransaction,
                    ArrayList(tagsToDelete)
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

    private fun initializeDateTimePicker(dateTime: Long?) {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        transactionCalendar = Calendar.getInstance()
        if (dateTime != null) {
            transactionCalendar.timeInMillis = dateTime
        }
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

    private fun populateForm(transactionId: Int) {
        transactionViewModel.getTransaction(transactionId).observe(viewLifecycleOwner, Observer {
            binding.editTransactionTitle.setText(it.transaction.title)
            binding.editTransactionDescription.setText(it.transaction.description)
            binding.editTransactionAmount.setText(DecimalFormat("0.00").format(it.transaction.amount))
            binding.editTransactionAmountDeduction.isChecked = it.transaction.amount < 0
            this.initializeDateTimePicker(it.transaction.dateTimeModified)

            val accountIdx = accounts.indexOfFirst { act -> act.id == it.transaction.accountId }
            binding.editTransactionAccountId.setSelection(accountIdx, true)

            it.tags.mapTo(currentTransactionTags) { itTag -> itTag }
            this.currentTransactionTags.forEach { t -> this.addChip(t) }
        })
    }

    private fun addChip(tag: Tag) {
        val chip = Chip(requireActivity()).apply {
            text = tag.name
            id = tag.tagId
            isCloseIconVisible = true
            setOnCloseIconClickListener {
                    chipIt -> (chipIt.parent as ChipGroup).removeView(chipIt)
            }
        }
        val existingChips = binding.editTransactionSelectedTagGroups.children
        val foundChip = existingChips.find { (it as Chip).text == chip.text }
        if (foundChip == null) {
            binding.editTransactionSelectedTagGroups.addView(chip)
        }
    }

    private fun checkForValidField(name: String, field: TextInputLayout, fieldName: String, checkForError: Boolean): Boolean {
        val hasError = if (fieldName == "Title") {
            name.isEmpty()
        } else {
            name.toDoubleOrNull() == null
        }

        if (hasError && checkForError) {
            field.error = "$fieldName is Required"
            field.isErrorEnabled = true
            return true
        } else if (!hasError) {
            field.error = null
            field.isErrorEnabled = false
        }
        return false
    }
}
