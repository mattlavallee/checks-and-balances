package io.github.mattlavallee.checksandbalances.navigation

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView
import io.github.mattlavallee.checksandbalances.R

class SettingsBottomSheet: BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val settingsView = inflater.inflate(R.layout.layout_settings_bottomsheet, container, false)

        val navigationView: NavigationView = settingsView.findViewById(R.id.settings_navigation_view)
        navigationView.setNavigationItemSelectedListener {
            if (it!!.itemId === R.id.settings_theme) {
                val isLightTheme = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO
                val theme = if(isLightTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                AppCompatDelegate.setDefaultNightMode(theme)
                super.dismiss()
            }
            true
        }

        return settingsView
    }
}
