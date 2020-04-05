package io.github.mattlavallee.checksandbalances.database

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.github.mattlavallee.checksandbalances.database.dao.AccountDao
import io.github.mattlavallee.checksandbalances.database.entities.Account

@Database(entities = [Account::class], version = 1)
abstract class ChecksAndBalancesDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao

    fun insert(account: Account) {
        InsertAccountAsyncTask(accountDao()).execute(account)
    }

    fun update(account: Account) {
        UpdateAccountAsyncTask(accountDao()).execute(account)
    }

    fun delete(id: Int) {
        DeleteAccountAsyncTask(accountDao()).execute(id)
    }

    private class InsertAccountAsyncTask(accountDao: AccountDao) : AsyncTask<Account, Unit, Unit>() {
        val accountDao = accountDao

        override fun doInBackground(vararg params: Account?) {
            accountDao.insertAccount(params[0]!!)
        }
    }

    private class UpdateAccountAsyncTask(accountDao: AccountDao): AsyncTask<Account, Unit, Unit>() {
        val accountDao = accountDao

        override fun doInBackground(vararg params: Account?) {
            accountDao.updateAccount(params[0]!!)
        }
    }

    private class DeleteAccountAsyncTask(accountDao: AccountDao) : AsyncTask<Int, Unit, Unit>() {
        val accountDao = accountDao

        override fun doInBackground(vararg params: Int?) {
            val accountToDelete = accountDao.getAccountById(params[0]!!)
            accountDao.delete(accountToDelete)
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