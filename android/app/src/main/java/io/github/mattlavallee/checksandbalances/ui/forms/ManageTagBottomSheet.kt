package io.github.mattlavallee.checksandbalances.ui.forms

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
import com.google.android.material.textfield.TextInputLayout
import io.github.mattlavallee.checksandbalances.R
import io.github.mattlavallee.checksandbalances.databinding.LayoutManageTagFormBinding
import io.github.mattlavallee.checksandbalances.ui.tags.TagViewModel

class ManageTagBottomSheet: BottomSheetDialogFragment() {
    private val tagViewModel: TagViewModel by activityViewModels()
    private lateinit var binding: LayoutManageTagFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CABBottomSheet)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val tagView = inflater.inflate(R.layout.layout_manage_tag_form, container, false)
        binding = LayoutManageTagFormBinding.bind(tagView)

        val tagId = arguments?.getInt("tagId")
        if (tagId == null) {
            super.dismiss()
            return tagView
        }
        this.populateForm(tagId)

        binding.editTagName.requestFocus()
        binding.editTagName.addTextChangedListener {
            this.checkForValidTagName(it.toString(), binding.editTagNameWrapper, false)
        }

        val inputMethodManager: InputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(binding.editTagName, 0)

        binding.editTagSaveButton.setOnClickListener {
            val name = binding.editTagName.text.toString()
            val hasError = this.checkForValidTagName(name, binding.editTagNameWrapper, true)
            if (hasError) {
                return@setOnClickListener
            }

            inputMethodManager.hideSoftInputFromWindow(tagView.windowToken, 0)
            this.tagViewModel.update(tagId, name, true)
            super.dismiss()
        }

        return tagView
    }

    private fun checkForValidTagName(name: String, field: TextInputLayout, checkForError: Boolean): Boolean {
        if (name.isNotEmpty()) {
            field.error = null
            field.isErrorEnabled = false
            return false
        } else if (checkForError) {
            field.isErrorEnabled = true
            field.error = "Tag Name is Required"
            return true
        }
        return false
    }

    private fun populateForm(tagId: Int) {
        tagViewModel.getTagById(tagId).observe(viewLifecycleOwner) {
            binding.editTagName.setText(it.name)
        }
    }
}
