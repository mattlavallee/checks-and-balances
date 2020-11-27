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
import io.github.mattlavallee.checksandbalances.database.entities.Account
import java.text.DecimalFormat

typealias Callback = (Int) -> Unit
class AccountAdapter(
    val onEdit: Callback,
    val onDelete: Callback,
    val onDrill: Callback): RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {
    private var accounts: ArrayList<Account> = ArrayList()

    class AccountViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardView: MaterialCardView = itemView.findViewById(R.id.account_card_view)
        val accountName: MaterialTextView = itemView.findViewById(R.id.account_card_view_name)
        val accountTotal: MaterialTextView = itemView.findViewById(R.id.account_card_view_transaction_total)
        val accountDescription: MaterialTextView = itemView.findViewById(R.id.account_card_view_description)
        val accountOverflow: MaterialTextView = itemView.findViewById(R.id.account_card_view_options_button)
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
        holder.accountTotal.text = "$" + DecimalFormat("0.00").format(account.startingBalance)

        holder.accountOverflow.tag = account.id
        holder.accountOverflow.setOnClickListener {
            val popupMenu = PopupMenu(it.context, it)
            val accountId: Int = it.tag as Int

            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                if (menuItem.itemId == R.id.action_popup_edit) {
                    this.onEdit(accountId)
                } else if (menuItem.itemId == R.id.action_popup_delete) {
                    this.onDelete(accountId)
                }

                false
            }
            popupMenu.show()
        }

        holder.cardView.setOnClickListener {
            onDrill(account.id)
        }
    }

    override fun getItemCount(): Int {
        return accounts.size
    }
}
