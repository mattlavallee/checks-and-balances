package io.github.mattlavallee.checksandbalances.database

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.github.mattlavallee.checksandbalances.database.dao.AccountDao
import io.github.mattlavallee.checksandbalances.database.dao.TransactionDao
import io.github.mattlavallee.checksandbalances.database.entities.Account
import io.github.mattlavallee.checksandbalances.database.entities.Transaction

@Database(entities = [Account::class, Transaction::class], version = 2)
abstract class ChecksAndBalancesDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun transactionDao(): TransactionDao

    fun insert(account: Account) {
        ChecksAndBalancesAsyncTask<Account>(accountDao(), "insert").execute(account)
    }

    fun insert(transaction: Transaction) {
        ChecksAndBalancesAsyncTask<Transaction>(transactionDao(), "insert").execute(transaction)
    }

    fun update(account: Account) {
        ChecksAndBalancesAsyncTask<Account>(accountDao(), "update").execute(account)
    }

    fun update(transaction: Transaction) {
        ChecksAndBalancesAsyncTask<Transaction>(transactionDao(), "update").execute(transaction)
    }

    fun deleteAccount(id: Int) {
        ChecksAndBalancesAsyncTask<Int>(accountDao(), "delete").execute(id)
    }

    fun deleteTransaction(id: Int) {
        ChecksAndBalancesAsyncTask<Int>(transactionDao(), "delete").execute(id)
    }

    private class ChecksAndBalancesAsyncTask<T>(dao: Any, type: String): AsyncTask<T, Unit, Unit>() {
        val dao = dao
        val actionType = type

        override fun doInBackground(vararg params: T?) {
            if (dao is AccountDao) {
                when (actionType) {
                    "insert" -> dao.insertAccount(params[0]!! as Account)
                    "update" -> dao.updateAccount(params[0]!! as Account)
                    "delete" -> {
                        val accountToDelete = dao.getAccountById(params[0]!! as Int)
                        dao.delete(accountToDelete)
                    }
                }
            } else if (dao is TransactionDao) {
                when (actionType) {
                    "insert" -> dao.insertTransaction(params[0]!! as Transaction)
                    "update" -> dao.updateTransaction(params[0]!! as Transaction)
                    "delete" -> {
                        val transactionToDelete = dao.getTransactionById(params[0]!! as Int)
                        dao.delete(transactionToDelete)
                    }
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