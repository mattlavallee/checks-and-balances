package io.github.mattlavallee.checksandbalances.database.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class TransactionWithTags(
    @Embedded val transaction: Transaction,
    @Relation(
        parentColumn = "transactionId",
        entityColumn = "tagId",
        associateBy = Junction(TransactionTagCrossRef::class)
    )
    val tags: List<Tag>
)

data class TagWithTransactions(
    @Embedded val tag: Tag,
    @Relation(
        parentColumn = "tagId",
        entityColumn = "transactionId",
        associateBy = Junction(TransactionTagCrossRef::class)
    )
    val transactions: List<Transaction>
)