package io.github.mattlavallee.checksandbalances.core.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.mattlavallee.checksandbalances.R
import io.github.mattlavallee.checksandbalances.core.Preferences
import io.github.mattlavallee.checksandbalances.core.TransactionSortFields
import io.github.mattlavallee.checksandbalances.database.entities.TransactionWithTags
import io.github.mattlavallee.checksandbalances.databinding.RecyclerTransactionRowBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TransactionAdapter(
    private val preferences: Preferences,
    val onEdit: Callback,
    val onArchive: Callback,
    val onDelete: Callback): RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {
    private var transactions: ArrayList<TransactionWithTags> = ArrayList()
    private val dateFormat = SimpleDateFormat("MMM dd yyyy", Locale.US)

    inner class TransactionViewHolder(val binding: RecyclerTransactionRowBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = RecyclerTransactionRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    fun setSortField(sortType: Int) {
        val sortField = TransactionSortFields.fromInt(sortType)
        transactions.sortWith { t1, t2 ->
            if (sortField == TransactionSortFields.Amount) {
                when {
                    t1.transaction.amount > t2.transaction.amount -> 1
                    t1.transaction.amount == t2.transaction.amount -> 0
                    else -> -1
                }
            } else if (sortField == TransactionSortFields.Description) {
                val t1desc = t1.transaction.description ?: ""
                val t2desc = t2.transaction.description ?: ""
                when {
                    t1desc > t2desc -> 1
                    t1desc == t2desc -> 0
                    else -> -1
                }
            } else if (sortField == TransactionSortFields.Date) {
                when {
                    t1.transaction.dateTimeModified > t2.transaction.dateTimeModified -> 1
                    t1.transaction.dateTimeModified == t2.transaction.dateTimeModified -> 0
                    else -> -1
                }
            } else {
                when {
                    t1.transaction.title > t2.transaction.title -> 1
                    t1.transaction.title == t2.transaction.title -> 0
                    else -> -1
                }
            }
        }
        this.notifyDataSetChanged()
    }

    fun setData(data: ArrayList<TransactionWithTags>) {
        val diffCallback = TransactionDiffCallback(transactions, data)
        val result = DiffUtil.calculateDiff(diffCallback)
        transactions.clear()
        transactions.addAll(data)
        result.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transWithTags = transactions[position]
        holder.binding.transactionCardViewTitle.text = transWithTags.transaction.title
        holder.binding.transactionCardViewTitle.tag = transWithTags.transaction.transactionId
        val currencyFormat = NumberFormat.getCurrencyInstance()
        currencyFormat.maximumFractionDigits = 2
        currencyFormat.currency = Currency.getInstance("USD")
        holder.binding.transactionCardViewAmount.text = currencyFormat.format(transWithTags.transaction.amount)
        if (transWithTags.transaction.amount != 0.0) {
            val color = if (transWithTags.transaction.amount >= 0) preferences.getPositiveColor() else preferences.getNegativeColor()
            holder.binding.transactionCardViewAmount.setTextColor(Color.parseColor(color))
        }

        holder.binding.transactionCardViewDateDescription.text = holder.itemView.context.getString(
            R.string.transaction_display_datetime_description,
            dateFormat.format(transWithTags.transaction.dateTimeModified),
            transWithTags.transaction.description
        )
        holder.binding.transactionCardView.tag = transWithTags.transaction.transactionId

        val tagNames = transWithTags.tags.sortedBy { it.name }.joinToString(", ") { it.name }
        holder.binding.transactionCardViewTags.text = tagNames

        holder.binding.transactionCardViewOptionsButton.tag = transWithTags.transaction.transactionId
        holder.binding.transactionCardViewOptionsButton.setOnClickListener {
            val popupMenu = PopupMenu(it.context, it)
            val transactionId: Int = it.tag as Int

            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                if (menuItem.itemId == R.id.action_popup_edit) {
                    this.onEdit(transactionId)
                } else if (menuItem.itemId == R.id.action_popup_archive) {
                    this.onArchive(transactionId)
                } else if (menuItem.itemId == R.id.action_popup_delete) {
                    this.onDelete(transactionId)
                }

                false
            }
            popupMenu.show()
        }
    }

    override fun getItemCount(): Int {
        return transactions.size
    }
}
