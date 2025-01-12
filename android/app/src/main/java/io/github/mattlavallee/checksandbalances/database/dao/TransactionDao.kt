package io.github.mattlavallee.checksandbalances.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import io.github.mattlavallee.checksandbalances.database.entities.Transaction
import io.github.mattlavallee.checksandbalances.database.entities.TransactionWithTags

@Dao
interface TransactionDao {
    @androidx.room.Transaction
    @Query("SELECT * FROM transactions WHERE is_active = 1 AND account_id = :accountId")
    fun getAllForAccount(accountId: Int): LiveData<List<TransactionWithTags>>

    @androidx.room.Transaction
    @Query("SELECT * FROM transactions WHERE transactionId = :transactionId")
    fun getLiveTransactionById(transactionId: Int): LiveData<TransactionWithTags>

    @androidx.room.Transaction
    @Query("SELECT * FROM transactions WHERE transactionId = :transactionId")
    fun getTransactionById(transactionId: Int): TransactionWithTags

    @androidx.room.Transaction
    @Query("SELECT * FROM transactions WHERE account_id = :accountId AND title = :title AND description = :descr")
    fun findTransaction(accountId: Int, title: String, descr: String): Transaction

    @Query("UPDATE transactions SET is_active = 0 WHERE transactionId = :transactionId")
    fun archiveTransaction(transactionId: Int)

    @Query("UPDATE transactions SET is_active = 0 where account_id = :accountId")
    fun archiveTransactionsForAccount(accountId: Int)

    @Query("DELETE FROM transactions WHERE transactionId = :transactionId")
    fun deleteTransaction(transactionId: Int)

    @androidx.room.Transaction
    @Query("DELETE FROM transactions WHERE is_active = 0 AND date_time_modified <= :sixMonthsAgo")
    fun autoCleanup(sixMonthsAgo: Long): Int

    @Insert
    fun insertTransaction(transaction: Transaction): Long

    @Update
    fun updateTransaction(transaction: Transaction): Int

    @Delete
    fun delete(transaction: Transaction)
}