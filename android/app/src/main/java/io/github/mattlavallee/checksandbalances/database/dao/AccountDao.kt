package io.github.mattlavallee.checksandbalances.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import io.github.mattlavallee.checksandbalances.database.entities.Account
import io.github.mattlavallee.checksandbalances.database.entities.AccountWithSum

@Dao
interface AccountDao {
    @Query("SELECT * FROM accounts WHERE is_active = 1")
    fun getAll(): LiveData<List<Account>>

    @Query("SELECT accounts.id, accounts.name, accounts.description, " +
            "accounts.starting_balance, accounts.is_active, SUM(transactions.amount) AS sum " +
            "FROM accounts LEFT OUTER JOIN transactions on accounts.id = transactions.account_id AND transactions.is_active = 1 " +
            "WHERE accounts.is_active = 1 GROUP BY accounts.id")
    fun getAllWithTransactionTotal(): LiveData<List<AccountWithSum>>

    @Query("SELECT * FROM accounts WHERE id = :accountId")
    fun getLiveAccountById(accountId: Int): LiveData<Account>

    @Query("SELECT * FROM accounts WHERE id = :accountId")
    fun getAccountById(accountId: Int): Account

    @Query("UPDATE accounts SET is_active = 0 WHERE id = :accountId")
    fun archiveAccount(accountId: Int)

    @Insert
    fun insertAccount(account: Account): Long

    @Update
    fun updateAccount(account: Account): Int

    @Delete
    fun delete(account: Account)
}