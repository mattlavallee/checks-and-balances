package io.github.mattlavallee.checksandbalances.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView
import io.github.mattlavallee.checksandbalances.R
import io.github.mattlavallee.checksandbalances.core.Preferences

class SettingsBottomSheet: BottomSheetDialogFragment() {
    private lateinit var appPreferences: Preferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val settingsView = inflater.inflate(R.layout.layout_settings_bottomsheet, container, false)
        appPreferences = Preferences(activity!!)

        val navigationView: NavigationView = settingsView.findViewById(R.id.settings_navigation_view)
        navigationView.setNavigationItemSelectedListener {
            if (it.itemId == R.id.settings_theme) {
                this.setAndSaveTheme()
                super.dismiss()
            }
            true
        }

        return settingsView
    }

    private fun setAndSaveTheme() {
        val currTheme = appPreferences.getTheme()
        val newTheme = if (currTheme == AppCompatDelegate.MODE_NIGHT_NO) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO

        AppCompatDelegate.setDefaultNightMode((newTheme))
        appPreferences.setTheme(newTheme)
    }
}
