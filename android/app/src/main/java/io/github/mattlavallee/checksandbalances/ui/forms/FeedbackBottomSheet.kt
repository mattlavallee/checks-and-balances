package io.github.mattlavallee.checksandbalances.ui.forms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.mattlavallee.checksandbalances.R
import io.github.mattlavallee.checksandbalances.databinding.LayoutFeedbackFormBinding

class FeedbackBottomSheet: BottomSheetDialogFragment() {
    private lateinit var binding: LayoutFeedbackFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CABBottomSheet)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val feedbackView = inflater.inflate(R.layout.layout_feedback_form, container, false)
        binding = LayoutFeedbackFormBinding.bind(feedbackView)

        binding.feedbackFeedback.text = HtmlCompat.fromHtml(getString(R.string.feedback_feedback), HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.feedbackCredits.text = HtmlCompat.fromHtml(getString(R.string.feedback_credits), HtmlCompat.FROM_HTML_MODE_LEGACY)
        return feedbackView
    }
}
