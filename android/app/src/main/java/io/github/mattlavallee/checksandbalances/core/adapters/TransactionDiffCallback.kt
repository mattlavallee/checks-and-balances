package io.github.mattlavallee.checksandbalances.core.adapters

import androidx.recyclerview.widget.DiffUtil
import io.github.mattlavallee.checksandbalances.database.entities.TransactionWithTags

class TransactionDiffCallback(
    private val oldList: ArrayList<TransactionWithTags>,
    private val newList: ArrayList<TransactionWithTags>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].transaction.transactionId == newList[newItemPosition].transaction.transactionId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        val oldTags = oldItem.tags.map { it.tagId }
        val newTags = newItem.tags.map { it.tagId }
        return oldItem.transaction.accountId == newItem.transaction.accountId &&
               oldItem.transaction.title == newItem.transaction.title &&
               oldItem.transaction.amount == newItem.transaction.amount &&
               oldItem.transaction.description == newItem.transaction.description &&
               oldItem.transaction.dateTimeModified == newItem.transaction.dateTimeModified &&
               oldTags.size == newTags.size && oldTags.none { !newTags.contains(it) }
    }
}

