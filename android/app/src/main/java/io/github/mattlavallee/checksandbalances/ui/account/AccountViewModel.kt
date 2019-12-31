package io.github.mattlavallee.checksandbalances.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AccountViewModel: ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "Account Fragment"
    }
    val text: LiveData<String> = _text
}
