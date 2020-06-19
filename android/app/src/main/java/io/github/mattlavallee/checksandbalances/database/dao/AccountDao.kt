package io.github.mattlavallee.checksandbalances.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import io.github.mattlavallee.checksandbalances.database.entities.Account

@Dao
interface AccountDao {
    @Query("SELECT * FROM accounts WHERE is_active = 1")
    fun getAll(): LiveData<List<Account>>

    @Query("SELECT * FROM accounts WHERE id = :accountId")
    fun getLiveAccountById(accountId: Int): LiveData<Account>

    @Query("SELECT * FROM accounts WHERE id = :accountId")
    fun getAccountById(accountId: Int): Account

    @Query("UPDATE accounts SET is_active = 0 WHERE id = :accountId")
    fun archiveAccount(accountId: Int)

    @Insert
    fun insertAccount(vararg account: Account)

    @Update
    fun updateAccount(vararg account: Account)

    @Delete
    fun delete(account: Account)
}