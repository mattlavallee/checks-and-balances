package io.github.mattlavallee.checksandbalances.ui.transactions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import io.github.mattlavallee.checksandbalances.database.ChecksAndBalancesDatabase
import io.github.mattlavallee.checksandbalances.database.entities.Tag
import io.github.mattlavallee.checksandbalances.database.entities.Transaction
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
        newTags: ArrayList<String>,
        existingTags: ArrayList<Int>
    ) {
        Thread {
            newTags.forEach { tagName ->
                repository.insert(Tag(0, tagName, true))
            }
            val tags = repository.tagDao().getTags()
            repository.insert(
                Transaction(
                    id,
                    accountId,
                    title,
                    amount,
                    description,
                    dateTime,
                    true
                )
            )
            val trans = repository.transactionDao().findTransaction(accountId, title, description)

            newTags.forEach { tagName ->
                val currTag = tags.find { t -> t.name === tagName }
                if (currTag != null && trans != null) {
                    repository.insert(trans.transactionId, currTag.tagId)
                }
            }
            existingTags.forEach { tagId ->
                repository.insert(trans.transactionId, tagId)
            }
        }.start()
    }

    fun update(
        id: Int,
        accountId: Int,
        title: String,
        amount: Double,
        description: String,
        dateTime: Long,
        newTags: ArrayList<String>,
        existingTags: ArrayList<Int>,
        isActive: Boolean = true
    ) {
        Thread {
            var trans = repository.transactionDao().findTransaction(accountId, title, description)
            val existingTransactionTagIds = repository.transTagDao().findTagsForTransaction(trans.transactionId)
            newTags.forEach { tagName ->
                repository.insert(Tag(0, tagName, true))
            }
            val tags = repository.tagDao().getTags()
            repository.update(Transaction(id, accountId, title, amount, description, dateTime, isActive))
            trans = repository.transactionDao().findTransaction(accountId, title, description)

            val newTagIds = ArrayList<Int>()
            newTags.forEach { tagName ->
                val currTag = tags.find { t -> t.name === tagName }
                if (currTag != null && trans != null) {
                    repository.insert(trans.transactionId, currTag.tagId)
                    newTagIds.add(currTag.tagId)
                }
            }
            existingTags.forEach { tagId ->
                repository.insert(trans.transactionId, tagId)
            }
            val deleteTags = existingTransactionTagIds.filter { transTag ->
                newTagIds.indexOf(transTag.tagId) < 0 && existingTags.indexOf(transTag.tagId) < 0
            }
            deleteTags.forEach { tags ->
                repository.delete(trans.transactionId, tags.tagId)
            }
        }.start()
    }

    fun archive(id: Int) {
        repository.archiveTransaction(id)
    }
}
