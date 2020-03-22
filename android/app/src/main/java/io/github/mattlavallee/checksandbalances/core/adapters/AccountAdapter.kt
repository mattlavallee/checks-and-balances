package io.github.mattlavallee.checksandbalances.core.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import io.github.mattlavallee.checksandbalances.R
import io.github.mattlavallee.checksandbalances.core.models.Account
import io.github.mattlavallee.checksandbalances.databinding.RecyclerAccountRowBinding

class AccountAdapter: RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {
    private var accounts: ArrayList<Account> = ArrayList()

    class AccountViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardView: MaterialCardView = itemView.findViewById(R.id.account_card_view)
        val accountName: MaterialTextView = itemView.findViewById(R.id.account_card_view_name)
        val accountTotal: MaterialTextView = itemView.findViewById(R.id.account_card_view_transaction_total)
        val accountDescription: MaterialTextView = itemView.findViewById(R.id.account_card_view_description)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, index: Int): AccountViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.recycler_account_row, viewGroup, false)
        return AccountViewHolder(view)
    }

    fun setData(data: ArrayList<Account>) {
        val diffCallback = AccountDiffCallback(accounts, data)
        val result = DiffUtil.calculateDiff(diffCallback)
        accounts.clear()
        accounts.addAll(data)
        result.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val account = accounts[position]
        holder.cardView.tag = account.id

        holder.accountName.text = account.name
        holder.accountName.tag = account.id
        holder.accountDescription.text = account.description

        holder.accountTotal.text = "$1000"
    }

    override fun getItemCount(): Int {
        return accounts.size
    }
}
