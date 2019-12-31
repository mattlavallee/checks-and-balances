package io.github.mattlavallee.checksandbalances.ui.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TransactionViewModel: ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "Transaction Fragment"
    }
    val text: LiveData<String> = _text
}
