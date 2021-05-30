package io.github.mattlavallee.checksandbalances.core.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.mattlavallee.checksandbalances.R
import io.github.mattlavallee.checksandbalances.database.entities.Transaction
import io.github.mattlavallee.checksandbalances.databinding.RecyclerTransactionRowBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TransactionAdapter(
    val onEdit: Callback,
    val onDelete: Callback): RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {
    private var transactions: ArrayList<Transaction> = ArrayList()
    private val dateFormat = SimpleDateFormat("MMM dd yyyy")

    inner class TransactionViewHolder(val binding: RecyclerTransactionRowBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = RecyclerTransactionRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    fun setData(data: ArrayList<Transaction>) {
        val diffCallback = TransactionDiffCallback(transactions, data)
        val result = DiffUtil.calculateDiff(diffCallback)
        transactions.clear()
        transactions.addAll(data)
        result.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.binding.transactionCardViewTitle.text = transaction.title
        holder.binding.transactionCardViewTitle.tag = transaction.id
        val currencyFormat = NumberFormat.getCurrencyInstance()
        currencyFormat.maximumFractionDigits = 2
        currencyFormat.currency = Currency.getInstance("USD")
        holder.binding.transactionCardViewAmount.text = currencyFormat.format(transaction.amount)
        holder.binding.transactionCardViewDateDescription.text = holder.itemView.context.getString(
            R.string.transaction_display_datetime_description,
            dateFormat.format(transaction.dateTimeModified),
            transaction.description
        )
        holder.binding.transactionCardView.tag = transaction.id

        holder.binding.transactionCardViewOptionsButton.tag = transaction.id
        holder.binding.transactionCardViewOptionsButton.setOnClickListener {
            val popupMenu = PopupMenu(it.context, it)
            val transactionId: Int = it.tag as Int

            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                if (menuItem.itemId == R.id.action_popup_edit) {
                    this.onEdit(transactionId)
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
