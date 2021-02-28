package io.github.mattlavallee.checksandbalances.ui.transactions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import io.github.mattlavallee.checksandbalances.database.ChecksAndBalancesDatabase
import io.github.mattlavallee.checksandbalances.database.entities.Transaction

class TransactionViewModel(application: Application): AndroidViewModel(application) {
    private var repository: ChecksAndBalancesDatabase = ChecksAndBalancesDatabase.getInstance(application.applicationContext)

    fun getTransaction(transactionId: Int): LiveData<Transaction> {
        return repository.transactionDao().getLiveTransactionById(transactionId)
    }

    fun getAccount(accountId: Int): LiveData<io.github.mattlavallee.checksandbalances.database.entities.Account> {
        return repository.accountDao().getLiveAccountById(accountId)
    }

    fun getTransactionsForAccount(accountId: Int): LiveData<List<Transaction>> {
        return repository.transactionDao().getAllForAccount(accountId)
    }
    
    fun save(
        id: Int,
        accountId: Int,
        title: String,
        amount: Double,
        description: String,
        dateTime: Long
    ) {
        repository.insert(Transaction(id, accountId, title, amount, description, dateTime, true))
    }

    fun update(
        id: Int,
        accountId: Int,
        title: String,
        amount: Double,
        description: String,
        dateTime: Long,
        isActive: Boolean = true
    ) {
        repository.update(Transaction(id, accountId, title, amount, description, dateTime, isActive))
    }

    fun archive(id: Int) {
        repository.archiveTransaction(id)
    }
}
