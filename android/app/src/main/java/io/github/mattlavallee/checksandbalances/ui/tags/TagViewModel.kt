package io.github.mattlavallee.checksandbalances.ui.tags

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import io.github.mattlavallee.checksandbalances.database.ChecksAndBalancesDatabase
import io.github.mattlavallee.checksandbalances.database.entities.Tag
import io.github.mattlavallee.checksandbalances.database.entities.TagWithTransactions

class TagViewModel(application: Application): AndroidViewModel(application) {
    private var repository: ChecksAndBalancesDatabase = ChecksAndBalancesDatabase.getInstance(application.applicationContext)

    fun getAllTags(): LiveData<List<Tag>> {
        return repository.tagDao().getAllTags()
    }

    fun getAllTagsWithTransactions(): LiveData<List<TagWithTransactions>> {
        return repository.tagDao().getAllTagsWithTransactions()
    }

    fun save(id: Int, name: String) {
        Thread {
            repository.insert(Tag(id, name, true))
        }.start()
    }

    fun archive(id: Int) {
        Thread {
            repository.archiveTag(id)
        }.start()
    }
}
