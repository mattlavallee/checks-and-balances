package io.github.mattlavallee.checksandbalances.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import io.github.mattlavallee.checksandbalances.database.entities.Transaction

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE is_active = 1 AND account_id = :accountId")
    fun getAllForAccount(accountId: Int): LiveData<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE id = :transactionId")
    fun getLiveTransactionById(transactionId: Int): LiveData<Transaction>

    @Query("SELECT * FROM transactions WHERE id = :transactionId")
    fun getTransactionById(transactionId: Int): Transaction

    @Query("UPDATE transactions SET is_active = 0 WHERE id = :transactionId")
    fun archiveTransaction(transactionId: Int)

    @Query("UPDATE transactions SET is_active = 0 where account_id = :accountId")
    fun archiveTransactionsForAccount(accountId: Int)

    @Insert
    fun insertTransaction(vararg transaction: Transaction)

    @Update
    fun updateTransaction(vararg transaction: Transaction)

    @Delete
    fun delete(vararg transaction: Transaction)
}