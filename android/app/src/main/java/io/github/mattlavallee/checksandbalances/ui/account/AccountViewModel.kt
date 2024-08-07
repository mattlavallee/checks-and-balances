package io.github.mattlavallee.checksandbalances.ui.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.mattlavallee.checksandbalances.database.ChecksAndBalancesDatabase
import io.github.mattlavallee.checksandbalances.database.entities.Account
import io.github.mattlavallee.checksandbalances.database.entities.AccountWithSum

class AccountViewModel(application: Application): AndroidViewModel(application) {
    private var repository: ChecksAndBalancesDatabase = ChecksAndBalancesDatabase.getInstance(application.applicationContext)

    val activeAccountId: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    fun getAllAccounts(): LiveData<List<Account>> {
        return repository.accountDao().getAll()
    }

    fun getAllAccountsWithSum(): LiveData<List<AccountWithSum>> {
        return repository.accountDao().getAllWithTransactionTotal()
    }

    fun getAccountById(accountId: Int): LiveData<Account> {
        return repository.accountDao().getLiveAccountById(accountId)
    }

    fun save(id: Int, name: String, description: String, startingBalance: Double) {
        Thread {
            repository.insert(Account(id, name, description, startingBalance, true))
        }.start()
    }

    fun update(id: Int, name: String, description: String, startingBalance: Double, isActive: Boolean) {
        Thread {
            repository.update(Account(id, name, description, startingBalance, isActive))
        }.start()
    }

    fun archive(id: Int) {
        if (this.activeAccountId.value == id) {
            this.activeAccountId.value = null
        }

        Thread {
            repository.archive(id)
        }.start()
    }
}
