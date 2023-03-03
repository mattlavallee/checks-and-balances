package io.github.mattlavallee.checksandbalances.core.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.removeItemAt
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import io.github.mattlavallee.checksandbalances.R
import io.github.mattlavallee.checksandbalances.core.AccountSortFields
import io.github.mattlavallee.checksandbalances.core.Preferences
import io.github.mattlavallee.checksandbalances.database.entities.Account
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

typealias Callback = (Int) -> Unit
class AccountAdapter(
    private val preferences: Preferences,
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

    fun setSortField(sortType: Int) {
        accounts.sortWith { a1, a2 ->
            if (AccountSortFields.fromInt(sortType) == AccountSortFields.Name) {
                when {
                    a1.name > a2.name -> 1
                    a1.name == a2.name -> 0
                    else -> -1
                }
            } else {
                //TODO: this should be total balance, not starting balance
                when {
                    a1.startingBalance > a2.startingBalance -> 1
                    a1.startingBalance == a2.startingBalance -> 0
                    else -> -1
                }
            }
        }
        this.notifyDataSetChanged()
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
        val currencyFormat = NumberFormat.getCurrencyInstance()
        currencyFormat.maximumFractionDigits = 2
        currencyFormat.currency = Currency.getInstance("USD")
        holder.accountTotal.text = currencyFormat.format(account.startingBalance)
        if (account.startingBalance != 0.0) {
            val color =
                if (account.startingBalance >= 0) preferences.getPositiveColor() else preferences.getNegativeColor()
            holder.accountTotal.setTextColor(Color.parseColor(color))
        }

        holder.accountOverflow.tag = account.id
        holder.accountOverflow.setOnClickListener {
            val popupMenu = PopupMenu(it.context, it)
            val accountId: Int = it.tag as Int

            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            popupMenu.menu.removeItemAt(2)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                if (menuItem.itemId == R.id.action_popup_edit) {
                    this.onEdit(accountId)
                } else if (menuItem.itemId == R.id.action_popup_archive) {
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
