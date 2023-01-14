package io.github.mattlavallee.checksandbalances.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import io.github.mattlavallee.checksandbalances.database.entities.Tag
import io.github.mattlavallee.checksandbalances.database.entities.TagWithTransactions

@Dao
interface TagDao {
    @Transaction
    @Query("SELECT * FROM tags WHERE is_active = 1")
    fun getAllTags(): LiveData<List<Tag>>

    @Transaction
    @Query("SELECT * FROM tags WHERE is_active = 1")
    fun getAllTagsWithTransactions(): LiveData<List<TagWithTransactions>>

    @Transaction
    @Query("SELECT * FROM tags WHERE tagId = :tagId")
    fun getTagById(tagId: Int): LiveData<Tag>

    @Transaction
    @Query("SELECT * FROM tags WHERE is_Active = 1")
    fun getTags(): List<Tag>

    @Query("UPDATE tags SET is_active = 0 WHERE tagId = :tagId")
    fun archiveTag(tagId: Int)

    @Insert
    fun insertTag(tag: Tag): Long

    @Insert
    fun insertTags(tags: List<Tag>): List<Long>

    @Update
    fun updateTag(tag: Tag): Int

    @Delete
    fun deleteTag(tag: Tag)
}
