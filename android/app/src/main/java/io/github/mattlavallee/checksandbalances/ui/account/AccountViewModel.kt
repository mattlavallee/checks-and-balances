package io.github.mattlavallee.checksandbalances.ui.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import io.github.mattlavallee.checksandbalances.database.ChecksAndBalancesDatabase

class AccountViewModel(application: Application): AndroidViewModel(application) {
    private var repository: ChecksAndBalancesDatabase = ChecksAndBalancesDatabase.getInstance(application.applicationContext)
    private var accounts: LiveData<List<io.github.mattlavallee.checksandbalances.database.entities.Account>> = repository.accountDao().getAll()

    fun getAllAccounts(): LiveData<List<io.github.mattlavallee.checksandbalances.database.entities.Account>> {
        return this.accounts
    }

    fun save(id: Int, name: String, description: String, startingBalance: Double) {
        repository.insert(
            io.github.mattlavallee.checksandbalances.database.entities.Account(
                id,
                name,
                description,
                startingBalance,
                true
            )
        )
    }

    fun update(id: Int, name: String, description: String, startingBalance: Double, isActive: Boolean) {
        repository.update(
            io.github.mattlavallee.checksandbalances.database.entities.Account(
                id,
                name,
                description,
                startingBalance,
                isActive
            )
        )
    }

    fun delete(accountId: Int) {
        repository.deleteAccount(accountId)
    }
}
