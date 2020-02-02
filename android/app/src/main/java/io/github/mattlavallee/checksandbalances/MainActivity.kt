package io.github.mattlavallee.checksandbalances

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.github.mattlavallee.checksandbalances.core.Constants
import io.github.mattlavallee.checksandbalances.core.Preferences
import io.github.mattlavallee.checksandbalances.ui.navigation.NavigationBottomSheet
import io.github.mattlavallee.checksandbalances.ui.navigation.SettingsBottomSheet

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preferences = Preferences(this)
        AppCompatDelegate.setDefaultNightMode(preferences.getTheme())

        val bottomAppBar: BottomAppBar = findViewById(R.id.navigation_app_bar)
        setSupportActionBar(bottomAppBar)
        bottomAppBar.replaceMenu(R.menu.bottom_nav_menu)
        bottomAppBar.setNavigationOnClickListener {
            val navigationBottomSheetMenu: NavigationBottomSheet =
                supportFragmentManager.findFragmentByTag(Constants.homeMenuTag) as NavigationBottomSheet?
                    ?: NavigationBottomSheet()
            navigationBottomSheetMenu.show(supportFragmentManager, Constants.homeMenuTag)
        }

        bottomAppBar.setOnMenuItemClickListener {
            if (it!!.itemId == R.id.navigation_settings) {
                val settingsBottomSheetMenu: SettingsBottomSheet =
                    supportFragmentManager.findFragmentByTag(Constants.settingsMenuTag) as SettingsBottomSheet?
                        ?: SettingsBottomSheet()
                settingsBottomSheetMenu.show(supportFragmentManager, Constants.settingsMenuTag)
            }
            true
        }

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            Log.i("CAB", "I'll do something eventually")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_nav_menu, menu)
        return true
    }
}
