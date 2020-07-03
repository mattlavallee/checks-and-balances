package io.github.mattlavallee.checksandbalances.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
class Transaction (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "account_id") val accountId: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "amount") val amount: Double,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "date_time_modified") val dateTimeModified: Long,
    @ColumnInfo(name = "is_active") val isActive: Boolean
)