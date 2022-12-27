package io.github.mattlavallee.checksandbalances.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.github.mattlavallee.checksandbalances.database.dao.AccountDao
import io.github.mattlavallee.checksandbalances.database.dao.TagDao
import io.github.mattlavallee.checksandbalances.database.dao.TransactionDao
import io.github.mattlavallee.checksandbalances.database.dao.TransactionTagDao
import io.github.mattlavallee.checksandbalances.database.entities.*

@Database(entities = [Account::class, Transaction::class, Tag::class, TransactionTagCrossRef::class], version = 1)
abstract class ChecksAndBalancesDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun transactionDao(): TransactionDao
    abstract fun tagDao(): TagDao
    abstract fun transTagDao(): TransactionTagDao

    fun insert(account: Account) {
        accountDao().insertAccount(account)
    }

    fun insert(transaction: Transaction): Long {
        return transactionDao().insertTransaction(transaction)
    }

    fun insert(tag: Tag): Long {
        return tagDao().insertTag(tag)
    }

    fun insert(tags: List<Tag>): List<Long> {
        return tagDao().insertTags(tags)
    }

    fun insert(transactionTags: List<TransactionTagCrossRef>) {
        transTagDao().insertTransactionTags(transactionTags)
    }

    fun update(account: Account) {
        accountDao().updateAccount(account)
    }

    fun update(transaction: Transaction) {
        transactionDao().updateTransaction(transaction)
    }

    fun delete(transactionId: Int, tagId: Int) {
        transTagDao().deleteTransactionTag(TransactionTagCrossRef(transactionId, tagId))
    }

    fun delete(transTags: List<TransactionTagCrossRef>) {
        transTagDao().deleteTags(transTags);
    }

    fun archive(accountId: Int) {
        accountDao().archiveAccount(accountId)
        transactionDao().archiveTransactionsForAccount(accountId)
    }

    fun archiveTransaction(transactionId: Int) {
        transactionDao().archiveTransaction(transactionId)
    }

    fun archiveTag(tagId: Int) {
        tagDao().archiveTag(tagId)
    }

    companion object {
        private var instance: ChecksAndBalancesDatabase? = null

        fun getInstance(context: Context): ChecksAndBalancesDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChecksAndBalancesDatabase::class.java,
                    "checks-and-balances"
                ).build()
            }

            return instance as ChecksAndBalancesDatabase
        }
    }
}