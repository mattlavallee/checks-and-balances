package io.github.mattlavallee.checksandbalances.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.mattlavallee.checksandbalances.core.models.Account

class AccountViewModel: ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "Account Fragment"
    }
    val text: LiveData<String> = _text

    fun save(newAccount: Account) {

    }
}
