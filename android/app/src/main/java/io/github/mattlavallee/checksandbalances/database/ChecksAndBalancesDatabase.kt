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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Account::class, Transaction::class, Tag::class, TransactionTagCrossRef::class], version = 3)
abstract class ChecksAndBalancesDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun transactionDao(): TransactionDao
    abstract fun tagDao(): TagDao
    abstract fun transTagDao(): TransactionTagDao

    fun insert(account: Account) {
        dbAction(accountDao(), "insert", account)
    }

    fun insert(transaction: Transaction) {
        dbAction(transactionDao(), "insert", transaction)
    }

    fun insert(tag: Tag) {
        dbAction(tagDao(), "insert", tag)
    }

    fun insert(transactionId: Int, tagId: Int) {
        dbAction(transTagDao(), "insert", TransactionTagCrossRef(transactionId, tagId))
    }

    fun update(account: Account) {
        dbAction(accountDao(), "update", account)
    }

    fun update(transaction: Transaction) {
        dbAction(transactionDao(), "update", transaction)
    }

    fun delete(transactionId: Int, tagId: Int) {
        dbAction(transTagDao(), "delete", TransactionTagCrossRef(transactionId, tagId))
    }

    fun archive(accountId: Int) {
        dbAction(accountDao(), "archive", accountId)
        dbAction(transactionDao(), "archiveAccount", accountId)
    }

    fun archiveTransaction(transactionId: Int) {
        dbAction(transactionDao(), "archive", transactionId)
    }

    fun archiveTag(tagId: Int) {
        dbAction(tagDao(), "archive", tagId)
    }

    fun deleteTag(transactionId: Int, tagId: Int) {
        dbAction(transTagDao(), "delete", TransactionTagCrossRef(transactionId, tagId))
    }

    private fun dbAction(dao: Any, type: String, item: Any) {
        CoroutineScope(Dispatchers.IO).launch {
            if (dao is AccountDao) {
                when (type) {
                    "insert" -> dao.insertAccount(item as Account)
                    "update" -> dao.updateAccount(item as Account)
                    "archive" -> dao.archiveAccount(item as Int)
                }
            } else if (dao is TransactionDao) {
                when (type) {
                    "insert" -> dao.insertTransaction(item as Transaction)
                    "update" -> dao.updateTransaction(item as Transaction)
                    "archive" -> dao.archiveTransaction(item as Int)
                    "archiveAccount" -> dao.archiveTransactionsForAccount(item as Int)
                }
            } else if (dao is TagDao) {
                when (type) {
                    "insert" -> dao.insertTag(item as Tag)
                    "archive" -> dao.archiveTag(item as Int)
                }
            } else if (dao is TransactionTagDao) {
                when (type) {
                    "insert" -> dao.insertTransactionTag(item as TransactionTagCrossRef)
                    "delete" -> dao.deleteTransactionTag(item as TransactionTagCrossRef)
                }
            }
        }
    }

    companion object {
        private var instance: ChecksAndBalancesDatabase? = null

        fun getInstance(context: Context): ChecksAndBalancesDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChecksAndBalancesDatabase::class.java,
                    "checks-and-balances"
                ).fallbackToDestructiveMigration().build()
            }

            return instance as ChecksAndBalancesDatabase
        }
    }
}