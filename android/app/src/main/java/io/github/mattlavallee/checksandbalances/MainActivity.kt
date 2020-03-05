package io.github.mattlavallee.checksandbalances

import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.github.mattlavallee.checksandbalances.core.Constants
import io.github.mattlavallee.checksandbalances.core.Preferences
import io.github.mattlavallee.checksandbalances.database.entities.Account
import io.github.mattlavallee.checksandbalances.databinding.ActivityMainBinding
import io.github.mattlavallee.checksandbalances.ui.account.AccountViewModel
import io.github.mattlavallee.checksandbalances.ui.navigation.FormDispatcher
import io.github.mattlavallee.checksandbalances.ui.navigation.NavigationBottomSheet
import io.github.mattlavallee.checksandbalances.ui.navigation.SettingsBottomSheet

class MainActivity : AppCompatActivity() {
    private val accountViewModel: AccountViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = Preferences(this)
        AppCompatDelegate.setDefaultNightMode(preferences.getTheme())

        setSupportActionBar(binding.navigationAppBar)
        binding.navigationAppBar.replaceMenu(R.menu.bottom_nav_menu)
        binding.navigationAppBar.setNavigationOnClickListener {
            val navigationBottomSheetMenu: NavigationBottomSheet =
                supportFragmentManager.findFragmentByTag(Constants.homeMenuTag) as NavigationBottomSheet?
                    ?: NavigationBottomSheet()
            navigationBottomSheetMenu.show(supportFragmentManager, Constants.homeMenuTag)
        }

        binding.navigationAppBar.setOnMenuItemClickListener {
            if (it!!.itemId == R.id.navigation_settings) {
                val settingsBottomSheetMenu: SettingsBottomSheet =
                    supportFragmentManager.findFragmentByTag(Constants.settingsMenuTag) as SettingsBottomSheet?
                        ?: SettingsBottomSheet()
                settingsBottomSheetMenu.show(supportFragmentManager, Constants.settingsMenuTag)
            }
            true
        }

        binding.fab.setOnClickListener {
            val accounts: List<Account>? = accountViewModel.getAllAccounts().value
            val tag: String = if (accounts != null && accounts.isNotEmpty()) Constants.transactionFormTag else Constants.accountFormTag
            FormDispatcher.launch(supportFragmentManager, tag)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_nav_menu, menu)
        return true
    }
}
