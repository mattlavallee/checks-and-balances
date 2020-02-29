package io.github.mattlavallee.checksandbalances.core.adapters

import androidx.recyclerview.widget.DiffUtil
import io.github.mattlavallee.checksandbalances.core.models.Account

class AccountDiffCallback(private val oldList: ArrayList<Account>, private val newList: ArrayList<Account>): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].hasSameContents(newList[newItemPosition])
    }
}
