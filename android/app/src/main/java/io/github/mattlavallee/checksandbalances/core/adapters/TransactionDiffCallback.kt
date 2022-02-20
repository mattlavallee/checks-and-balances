package io.github.mattlavallee.checksandbalances.core.adapters

import androidx.recyclerview.widget.DiffUtil
import io.github.mattlavallee.checksandbalances.database.entities.Transaction

class TransactionDiffCallback(
    private val oldList: ArrayList<Transaction>,
    private val newList: ArrayList<Transaction>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].transactionId == newList[newItemPosition].transactionId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem.accountId == newItem.accountId &&
               oldItem.title == newItem.title &&
               oldItem.amount == newItem.amount &&
               oldItem.description == newItem.description &&
               oldItem.dateTimeModified == newItem.dateTimeModified
    }
}

