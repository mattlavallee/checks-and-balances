package io.github.mattlavallee.checksandbalances.ui.tags

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import io.github.mattlavallee.checksandbalances.database.ChecksAndBalancesDatabase
import io.github.mattlavallee.checksandbalances.database.entities.Tag

class TagViewModel(application: Application): AndroidViewModel(application) {
    private var repository: ChecksAndBalancesDatabase = ChecksAndBalancesDatabase.getInstance(application.applicationContext)

    fun getAllTags(): LiveData<List<Tag>> {
        return repository.tagDao().getAllTags()
    }

    fun save(id: Int, name: String) {
        repository.insert(Tag(id, name, true))
    }

    fun archive(id: Int) {
        repository.archiveTag(id)
    }
}
