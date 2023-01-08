package io.github.mattlavallee.checksandbalances.database.dao

import androidx.room.*
import io.github.mattlavallee.checksandbalances.database.entities.TransactionTagCrossRef

@Dao
interface TransactionTagDao {
    @Insert
    fun insertTransactionTag(transTag: TransactionTagCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTransactionTags(transactionTags: List<TransactionTagCrossRef>)

    @Delete
    fun deleteTransactionTag(transTag: TransactionTagCrossRef)

    @Delete
    fun deleteTags(transTags: List<TransactionTagCrossRef>)

    @Transaction
    @Query("DELETE FROM transactionTags WHERE tagId = :tagId")
    fun removeByTagId(tagId: Int)

    @Transaction
    @Query("SELECT * FROM transactionTags WHERE transactionId = :transactionId")
    fun findTagsForTransaction(transactionId: Int): List<TransactionTagCrossRef>
}
