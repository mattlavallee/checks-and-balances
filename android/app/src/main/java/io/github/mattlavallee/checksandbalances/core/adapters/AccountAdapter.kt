package io.github.mattlavallee.checksandbalances.core.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import io.github.mattlavallee.checksandbalances.R
import io.github.mattlavallee.checksandbalances.core.models.Account

class AccountAdapter(allAccounts: ArrayList<Account>): RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {
    private var accounts: ArrayList<Account> = allAccounts

    class AccountViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var cardView: MaterialCardView = itemView.findViewById(R.id.account_card_view)
        var accountName: MaterialTextView = itemView.findViewById(R.id.account_card_view_name)
        var accountTotal: MaterialTextView = itemView.findViewById(R.id.account_card_view_transaction_total)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, index: Int): AccountViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.recycler_account_row, viewGroup, false)
        return AccountViewHolder(view)
    }

    fun updateData(data: ArrayList<Account>) {
        accounts.clear()
        accounts = data
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val account = accounts[position]
        holder.cardView.tag = account.id

        holder.accountName.text = account.name
        holder.accountName.tag = account.id

        holder.accountTotal.text = "1000"
    }

    override fun getItemCount(): Int {
        return accounts.size
    }
}
