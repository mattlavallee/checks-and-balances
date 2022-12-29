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
    private val defaultNegativeColor = "#f44336"

    val preferences: SharedPreferences

    init {
        preferences = activity.getSharedPreferences(prefKey, Context.MODE_PRIVATE)
    }

    private fun getDefaultPositiveColor(): String {
        val theme = this.getTheme()
        return if (theme == AppCompatDelegate.MODE_NIGHT_NO) "#737373" else "#bfbfbf"
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

    fun getPositiveColor(): String {
        val defaultPositive = this.getDefaultPositiveColor()
        return preferences.getString(Constants.positiveColorKey, defaultPositive) ?: defaultPositive
    }

    fun getNegativeColor(): String {
        return preferences.getString(Constants.negativeColorKey, defaultNegativeColor) ?: defaultNegativeColor
    }

    fun setColor(colorKey: String, color: String) {
        var savedColor: String? = color;
        if (savedColor?.lowercase() == defaultNegativeColor || savedColor?.lowercase() == this.getDefaultPositiveColor()) {
            savedColor = null
        }
        with(preferences.edit()) {
            putString(colorKey, savedColor)
            apply()
        }
    }
}