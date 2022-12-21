package io.github.mattlavallee.checksandbalances.database.entities

import androidx.room.Entity

@Entity(primaryKeys = ["transactionId", "tagId"], tableName = "transactionTags")
class TransactionTagCrossRef(
    val transactionId: Int,
    val tagId : Int
)