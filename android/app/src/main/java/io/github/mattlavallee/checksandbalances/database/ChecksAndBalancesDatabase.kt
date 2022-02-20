package io.github.mattlavallee.checksandbalances.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.github.mattlavallee.checksandbalances.database.dao.AccountDao
import io.github.mattlavallee.checksandbalances.database.dao.TransactionDao
import io.github.mattlavallee.checksandbalances.database.entities.Account
import io.github.mattlavallee.checksandbalances.database.entities.Tag
import io.github.mattlavallee.checksandbalances.database.entities.Transaction
import io.github.mattlavallee.checksandbalances.database.entities.TransactionTagCrossRef
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Account::class, Transaction::class, Tag::class, TransactionTagCrossRef::class], version = 2)
abstract class ChecksAndBalancesDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun transactionDao(): TransactionDao

    fun insert(account: Account) {
        dbAction(accountDao(), "insert", account)
    }

    fun insert(transaction: Transaction) {
        dbAction(transactionDao(), "insert", transaction)
    }

    fun update(account: Account) {
        dbAction(accountDao(), "update", account)
    }

    fun update(transaction: Transaction) {
        dbAction(transactionDao(), "update", transaction)
    }

    fun archive(accountId: Int) {
        dbAction(accountDao(), "archive", accountId)
        dbAction(transactionDao(), "archiveAccount", accountId)
    }

    fun archiveTransaction(transactionId: Int) {
        dbAction(transactionDao(), "archive", transactionId)
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