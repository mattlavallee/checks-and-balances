package io.github.mattlavallee.checksandbalances.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import io.github.mattlavallee.checksandbalances.database.entities.Tag

@Dao
interface TagDao {
    @androidx.room.Transaction
    @Query("SELECT * FROM tags WHERE is_active = 1")
    fun getAllTags(): LiveData<List<Tag>>

    @androidx.room.Transaction
    @Query("SELECT * FROM tags WHERE is_Active = 1")
    fun getTags(): List<Tag>

    @Query("UPDATE tags SET is_active = 0 WHERE tagId = :tagId")
    fun archiveTag(tagId: Int)

    @Insert
    fun insertTag(vararg tag: Tag)

    @Delete
    fun deleteTag(vararg tag: Tag)
}
