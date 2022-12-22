package io.github.mattlavallee.checksandbalances.database.entities

import androidx.room.Entity
import androidx.room.Index

@Entity(
    primaryKeys = ["transactionId", "tagId"],
    tableName = "transactionTags",
    indices = [Index(value=["transactionId", "tagId"])]
)
class TransactionTagCrossRef(
    val transactionId: Int,
    val tagId : Int
)