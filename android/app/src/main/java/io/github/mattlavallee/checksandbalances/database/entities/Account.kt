package io.github.mattlavallee.checksandbalances.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
class Account (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "starting_balance") val startingBalance: Double,
    @ColumnInfo(name = "is_active") val isActive: Boolean
)