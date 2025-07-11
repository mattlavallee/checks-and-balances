package io.github.mattlavallee.checksandbalances

import android.os.Bundle
import android.view.Menu
import android.view.WindowInsets
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.updatePadding
import androidx.lifecycle.Observer
import io.github.mattlavallee.checksandbalances.core.Constants
import io.github.mattlavallee.checksandbalances.core.Preferences
import io.github.mattlavallee.checksandbalances.databinding.ActivityMainBinding
import io.github.mattlavallee.checksandbalances.ui.account.AccountViewModel
import io.github.mattlavallee.checksandbalances.ui.navigation.FormDispatcher
import io.github.mattlavallee.checksandbalances.ui.navigation.NavigationBottomSheet
import io.github.mattlavallee.checksandbalances.ui.navigation.SettingsBottomSheet
import io.github.mattlavallee.checksandbalances.ui.transactions.TransactionViewModel
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private val accountViewModel: AccountViewModel by viewModels()
    private val transactionViewModel: TransactionViewModel by viewModels()
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
                val stackCount = supportFragmentManager.backStackEntryCount
                val currFragment = if (stackCount > 0) supportFragmentManager.getBackStackEntryAt(stackCount - 1) else null
                val isTransactionFragment = currFragment?.name == Constants.transactionViewTag
                settingsBottomSheetMenu.setIsTransactionViewVisible(isTransactionFragment)
                settingsBottomSheetMenu.setMenuDisplayText()
                settingsBottomSheetMenu.show(supportFragmentManager, Constants.settingsMenuTag)
            }
            true
        }

        var accountCount = 0
        accountViewModel.getAllAccounts().observe(this, Observer {itList ->
            accountCount = itList.size
        })


        val sixMonthsAgo = Calendar.getInstance()
        sixMonthsAgo.add(Calendar.DAY_OF_YEAR, -185);
        transactionViewModel.autoCleanup(sixMonthsAgo.timeInMillis)

        binding.fab.setOnClickListener {
            val tag: String = if (accountCount > 0) Constants.transactionFormTag else Constants.accountFormTag
            var bundle: Bundle? = null
            if (accountViewModel.activeAccountId.value != null) {
                bundle = Bundle()
                bundle.putInt("transactionId", -1)
                bundle.putInt("accountId", accountViewModel.activeAccountId.value!!)
            }

            FormDispatcher.launch(supportFragmentManager, tag, bundle)
        }

        binding.navHostFragment.setOnApplyWindowInsetsListener { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsets.Type.systemBars())
            view.updatePadding(top = insets.top, bottom = insets.bottom)
            windowInsets
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_nav_menu, menu)
        return true
    }
}
