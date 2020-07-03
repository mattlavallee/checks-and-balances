package io.github.mattlavallee.checksandbalances.ui.forms

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.mattlavallee.checksandbalances.R
import io.github.mattlavallee.checksandbalances.databinding.LayoutTransactionFormBinding
import io.github.mattlavallee.checksandbalances.ui.transactions.TransactionViewModel

class TransactionBottomSheet: BottomSheetDialogFragment() {
    private val transactionViewModel: TransactionViewModel by activityViewModels()
    private lateinit var binding: LayoutTransactionFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CABBottomSheet)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val transactionView = inflater.inflate(R.layout.layout_transaction_form, container, false)
        binding = LayoutTransactionFormBinding.bind(transactionView)

        val transactionId = arguments?.getInt("transactionId")
        if (transactionId != null) {
            this.populateForm(transactionId)
        }

        binding.editTransactionTitle.requestFocus()

        val inputMethodManager: InputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)

        return transactionView
    }

    private fun populateForm(transactionId: Int) {}
}
