package io.github.mattlavallee.checksandbalances.ui.tags

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import io.github.mattlavallee.checksandbalances.R
import io.github.mattlavallee.checksandbalances.core.Constants
import io.github.mattlavallee.checksandbalances.databinding.FragmentManageTagsBinding
import io.github.mattlavallee.checksandbalances.ui.navigation.FormDispatcher


class ManageTagsFragment: Fragment() {
    private val tagViewModel: TagViewModel by activityViewModels()
    private lateinit var binding: FragmentManageTagsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_manage_tags, container, false)
        binding = FragmentManageTagsBinding.bind(root)

        tagViewModel.getAllTagsWithTransactions().observe(viewLifecycleOwner, Observer { itList ->
            binding.manageTagsGroup.removeAllViews()
            itList.sortedBy { it.tag.name.lowercase() }.forEach {
                val activeTransactions = it.transactions.count { t -> t.isActive }
                val chip = Chip(requireActivity()).apply {
                    text = getString(R.string.manage_tags_label, it.tag.name, activeTransactions)
                    id = it.tag.tagId
                    isCloseIconVisible = true
                    setOnCloseIconClickListener { chipIt ->
                        deleteTag(chipIt.id)
                    }
                    setOnClickListener { chipIt ->
                        editTag(chipIt.id)
                    }
                }
                binding.manageTagsGroup.addView(chip)
            }
        })
        return root
    }

    private fun editTag(tagId: Int) {
        val bundle = Bundle()
        bundle.putInt("tagId", tagId)
        FormDispatcher.launch(requireActivity().supportFragmentManager, Constants.manageTagsFormTag, bundle)
    }

    private fun deleteTag(tagId: Int) {
        tagViewModel.archive(tagId)
    }
}
