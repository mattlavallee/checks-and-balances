package io.github.mattlavallee.checksandbalances.core

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class Preferences(activity: Activity) {
    private val prefKey = "io.github.mattlavallee.checksandbalances.settings"
    private val defaultTheme = AppCompatDelegate.MODE_NIGHT_NO
    private val themeKey = "theme"
    private val accountSortField = "accountSort"
    private val transactionSortField = "transactionSort"

    val preferences: SharedPreferences

    init {
        preferences = activity.getSharedPreferences(prefKey, Context.MODE_PRIVATE)
    }

    fun getTheme(): Int {
        return preferences.getInt(themeKey, defaultTheme)
    }

    fun setTheme(newTheme: Int) {
        with(preferences.edit()) {
            putInt(themeKey, newTheme)
            apply()
        }
    }

    fun getAccountSortField(): Int {
        return preferences.getInt(accountSortField, AccountSortFields.Name.id)
    }

    fun setAccountSortField(sortField: Int) {
        with(preferences.edit()) {
            putInt(accountSortField, sortField)
            apply()
        }
    }

    fun getTransactionSortField(): Int {
        return preferences.getInt(transactionSortField, TransactionSortFields.Date.id)
    }

    fun setTransactionSortField(sortField: Int) {
        with(preferences.edit()) {
            putInt(transactionSortField, sortField)
            apply()
        }
    }
}