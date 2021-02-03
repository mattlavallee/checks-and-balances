package io.github.mattlavallee.checksandbalances.core.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import io.github.mattlavallee.checksandbalances.R
import io.github.mattlavallee.checksandbalances.database.entities.Transaction
import kotlinx.android.synthetic.main.recycler_transaction_row.view.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TransactionAdapter(): RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {
    private var transactions: ArrayList<Transaction> = ArrayList()
    private val dateFormat = SimpleDateFormat("MMM dd yyyy")

    class TransactionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardView: MaterialCardView = itemView.findViewById(R.id.transaction_card_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_transaction_row, parent, false)
        return TransactionViewHolder(view)
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
        holder.cardView.transaction_card_view_title.text = transaction.title
        val currencyFormat = NumberFormat.getCurrencyInstance()
        currencyFormat.maximumFractionDigits = 2
        currencyFormat.currency = Currency.getInstance("USD")
        holder.cardView.transaction_card_view_amount.text = currencyFormat.format(transaction.amount)
        holder.cardView.transaction_card_view_date_description.text = holder.cardView.context.getString(
            R.string.transaction_display_datetime_description,
            dateFormat.format(transaction.dateTimeModified),
            transaction.description
        );
        holder.cardView.tag = transaction.id
    }

    override fun getItemCount(): Int {
        return transactions.size
    }
}
