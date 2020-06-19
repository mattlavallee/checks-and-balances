package io.github.mattlavallee.checksandbalances.ui.transactions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import io.github.mattlavallee.checksandbalances.database.ChecksAndBalancesDatabase

class TransactionViewModel(application: Application): AndroidViewModel(application) {
    private var repository: ChecksAndBalancesDatabase = ChecksAndBalancesDatabase.getInstance(application.applicationContext)

    fun getAccount(accountId: Int): LiveData<io.github.mattlavallee.checksandbalances.database.entities.Account> {
        return repository.accountDao().getLiveAccountById(accountId)
    }
}
