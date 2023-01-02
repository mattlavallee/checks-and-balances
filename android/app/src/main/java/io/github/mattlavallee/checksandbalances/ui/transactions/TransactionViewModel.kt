package io.github.mattlavallee.checksandbalances.ui.transactions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import io.github.mattlavallee.checksandbalances.database.ChecksAndBalancesDatabase
import io.github.mattlavallee.checksandbalances.database.entities.Tag
import io.github.mattlavallee.checksandbalances.database.entities.Transaction
import io.github.mattlavallee.checksandbalances.database.entities.TransactionTagCrossRef
import io.github.mattlavallee.checksandbalances.database.entities.TransactionWithTags

class TransactionViewModel(application: Application): AndroidViewModel(application) {
    private var repository: ChecksAndBalancesDatabase = ChecksAndBalancesDatabase.getInstance(application.applicationContext)

    fun getTransaction(transactionId: Int): LiveData<TransactionWithTags> {
        return repository.transactionDao().getLiveTransactionById(transactionId)
    }

    fun getAccount(accountId: Int): LiveData<io.github.mattlavallee.checksandbalances.database.entities.Account> {
        return repository.accountDao().getLiveAccountById(accountId)
    }

    fun getTransactionsForAccount(accountId: Int): LiveData<List<TransactionWithTags>> {
        return repository.transactionDao().getAllForAccount(accountId)
    }
    
    fun save(
        id: Int,
        accountId: Int,
        title: String,
        amount: Double,
        description: String,
        dateTime: Long,
        tags: ArrayList<Tag>
    ) {
        Thread {
            val newTags = tags.filter { it.tagId == 0 }
            val tagIds = repository.insert(newTags)
            val transId = repository.insert(Transaction(id, accountId, title, amount, description, dateTime, true))
            val transTags = ArrayList(tagIds.map { TransactionTagCrossRef(transId.toInt(), it.toInt()) })
            tags.forEach {
                if (it.tagId > 0) {
                    transTags.add(TransactionTagCrossRef(transId.toInt(), it.tagId))
                }
            }
            repository.insert(transTags)
        }.start()
    }

    fun update(
        id: Int,
        accountId: Int,
        title: String,
        amount: Double,
        description: String,
        dateTime: Long,
        tagsToAdd: ArrayList<Tag>,
        tagsToDelete: ArrayList<Tag>,
        isActive: Boolean = true
    ) {
        Thread {
            repository.update(Transaction(id, accountId, title, amount, description, dateTime, isActive))

            val newTags = tagsToAdd.filter { it.tagId == 0 }
            val tagIds = repository.insert(newTags)

            val transTagsToAdd = ArrayList(tagIds.map { TransactionTagCrossRef(id, it.toInt())})
            tagsToAdd.forEach {
                if (it.tagId > 0) {
                    transTagsToAdd.add(TransactionTagCrossRef(id, it.tagId))
                }
            }
            repository.insert(transTagsToAdd)

            repository.delete(tagsToDelete.map { TransactionTagCrossRef(id, it.tagId) })
        }.start()
    }

    fun archive(id: Int) {
        Thread {
            repository.archiveTransaction(id)
        }.start()
    }

    fun archiveAll(accountId: Int) {
        Thread {
            repository.archiveTransactions(accountId)
        }.start()
    }
}
