package io.github.mattlavallee.checksandbalances.core

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class Preferences {
    private val prefKey = "io.github.mattlavallee.checksandbalances.settings"
    private val defaultTheme = AppCompatDelegate.MODE_NIGHT_NO
    private val themeKey = "theme"

    private val preferences: SharedPreferences

    constructor(activity: Activity) {
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
}