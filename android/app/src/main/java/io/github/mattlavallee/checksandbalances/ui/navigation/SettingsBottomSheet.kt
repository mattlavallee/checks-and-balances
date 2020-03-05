package io.github.mattlavallee.checksandbalances.ui.navigation

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textview.MaterialTextView
import io.github.mattlavallee.checksandbalances.R
import io.github.mattlavallee.checksandbalances.core.Preferences
import io.github.mattlavallee.checksandbalances.databinding.LayoutSettingsBottomsheetBinding

class SettingsBottomSheet: BottomSheetDialogFragment() {
    private lateinit var appPreferences: Preferences
    private lateinit var binding: LayoutSettingsBottomsheetBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val settingsView = inflater.inflate(R.layout.layout_settings_bottomsheet, container, false)
        binding = LayoutSettingsBottomsheetBinding.bind(settingsView)
        appPreferences = Preferences(activity!!)

        binding.settingsNavigationView.setNavigationItemSelectedListener {
            if (it.itemId == R.id.settings_theme) {
                this.setAndSaveTheme()
                super.dismiss()
            }
            true
        }

        val themeActionLayout: LinearLayout = binding.settingsNavigationView.menu.findItem(R.id.settings_theme).actionView as LinearLayout
        val currThemeView: MaterialTextView = themeActionLayout.findViewById(R.id.settings_current_theme_name)
        val currTheme = if (appPreferences.getTheme() == AppCompatDelegate.MODE_NIGHT_NO) "Light" else "Dark"
        currThemeView.text = currTheme
        return settingsView
    }

    private fun setAndSaveTheme() {
        val currTheme = appPreferences.getTheme()
        val newTheme = if (currTheme == AppCompatDelegate.MODE_NIGHT_NO) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO

        AppCompatDelegate.setDefaultNightMode((newTheme))
        appPreferences.setTheme(newTheme)
    }
}
