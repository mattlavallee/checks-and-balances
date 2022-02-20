package io.github.mattlavallee.checksandbalances.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tags")
class Tag (
    @PrimaryKey(autoGenerate = true)val tagId: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "is_active")val isActive: Boolean
)
