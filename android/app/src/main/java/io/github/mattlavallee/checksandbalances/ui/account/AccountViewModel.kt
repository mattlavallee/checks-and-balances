package io.github.mattlavallee.checksandbalances.ui.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import io.github.mattlavallee.checksandbalances.core.models.Account
import io.github.mattlavallee.checksandbalances.database.ChecksAndBalancesDatabase

class AccountViewModel(application: Application): AndroidViewModel(application) {
    private var repository: ChecksAndBalancesDatabase = ChecksAndBalancesDatabase.getInstance(application.applicationContext)
    private var accounts: LiveData<List<io.github.mattlavallee.checksandbalances.database.entities.Account>> = repository.accountDao().getAll()

    fun getAllAccounts(): LiveData<List<io.github.mattlavallee.checksandbalances.database.entities.Account>> {
        return this.accounts
    }

    fun save(newAccount: Account) {
        repository.insert(
            io.github.mattlavallee.checksandbalances.database.entities.Account(
                newAccount.id,
                newAccount.name,
                newAccount.description,
                newAccount.startingBalance,
                newAccount.isActive
            )
        )
    }

    fun update(updatedAccount: Account) {
        repository.update(
            io.github.mattlavallee.checksandbalances.database.entities.Account(
                updatedAccount.id,
                updatedAccount.name,
                updatedAccount.description,
                updatedAccount.startingBalance,
                updatedAccount.isActive
            )
        )
    }

    fun delete(accountId: Int) {
        repository.delete(accountId)
    }
}
