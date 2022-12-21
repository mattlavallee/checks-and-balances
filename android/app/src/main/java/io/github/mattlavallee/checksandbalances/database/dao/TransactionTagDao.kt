package io.github.mattlavallee.checksandbalances.database.dao

import androidx.room.*
import io.github.mattlavallee.checksandbalances.database.entities.TransactionTagCrossRef

@Dao
interface TransactionTagDao {
    @Insert
    fun insertTransactionTag(vararg transTag: TransactionTagCrossRef)

    @Delete
    fun deleteTransactionTag(vararg transTag: TransactionTagCrossRef)


    @Transaction
    @Query("SELECT * FROM transactionTags WHERE transactionId = :transactionId")
    fun findTagsForTransaction(transactionId: Int): List<TransactionTagCrossRef>
}
