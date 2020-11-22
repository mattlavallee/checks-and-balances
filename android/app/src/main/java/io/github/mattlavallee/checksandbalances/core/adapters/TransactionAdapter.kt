package io.github.mattlavallee.checksandbalances.core.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import io.github.mattlavallee.checksandbalances.R
import io.github.mattlavallee.checksandbalances.database.entities.Transaction
import kotlinx.android.synthetic.main.recycler_transaction_row.view.*

class TransactionAdapter(): RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {
    private var transactions: ArrayList<Transaction> = ArrayList()

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
        holder.cardView.transactoin_card_view_title.text = transaction.title
        holder.cardView.tag = transaction.id
    }

    override fun getItemCount(): Int {
        return transactions.size
    }
}
