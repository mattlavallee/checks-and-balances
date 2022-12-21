package io.github.mattlavallee.checksandbalances.ui.navigation

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textview.MaterialTextView
import io.github.mattlavallee.checksandbalances.R
import io.github.mattlavallee.checksandbalances.core.AccountSortFields
import io.github.mattlavallee.checksandbalances.core.Preferences
import io.github.mattlavallee.checksandbalances.core.TransactionSortFields
import io.github.mattlavallee.checksandbalances.databinding.LayoutSettingsBottomsheetBinding

class SettingsBottomSheet: BottomSheetDialogFragment() {
    private lateinit var appPreferences: Preferences
    private lateinit var binding: LayoutSettingsBottomsheetBinding
    private var isTransactionViewVisible = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val settingsView = inflater.inflate(R.layout.layout_settings_bottomsheet, container, false)
        binding = LayoutSettingsBottomsheetBinding.bind(settingsView)
        appPreferences = Preferences(requireActivity())

        this.setSortByDisplayName();

        binding.settingsNavigationView.setNavigationItemSelectedListener {
            if (it.itemId == R.id.settings_theme) {
                this.setAndSaveTheme()
                super.dismiss()
            } else if (it.itemId == R.id.settings_sort) {
                activity?.let { itActivity ->
                    val itemsArray = if (this.isTransactionViewVisible) R.array.app_settings_transaction_sortType else R.array.app_settings_account_sortType
                    AlertDialog.Builder(itActivity)
                        .setTitle("Sort By")
                        .setItems(itemsArray, DialogInterface.OnClickListener { dialog, which ->
                            if (this.isTransactionViewVisible) {
                                appPreferences.setTransactionSortField(which)
                            } else {
                                appPreferences.setAccountSortField(which)
                            }
                            dialog.dismiss()
                            super.dismiss()
                        })
                        .create()
                        .show()
                }
            }
            true
        }

        val themeActionLayout: LinearLayout = binding.settingsNavigationView.menu.findItem(R.id.settings_theme).actionView as LinearLayout
        val currThemeView: MaterialTextView = themeActionLayout.findViewById(R.id.settings_current_theme_name)
        val currTheme = if (appPreferences.getTheme() == AppCompatDelegate.MODE_NIGHT_NO) "Light" else "Dark"
        currThemeView.text = currTheme
        return settingsView
    }

    fun setIsTransactionViewVisible(isTransactionFragment: Boolean) {
        this.isTransactionViewVisible = isTransactionFragment
    }

    fun setSortByDisplayName() {
        if (!::binding.isInitialized) {
            return
        }
        val sortMenuItem = binding.settingsNavigationView.menu.getItem(0)
        val sortByFieldText = sortMenuItem.actionView?.findViewById<MaterialTextView>(R.id.settings_current_sort_by_name)
        val sortFieldId = if (this.isTransactionViewVisible) appPreferences.getTransactionSortField() else appPreferences.getAccountSortField()

        val text = if (this.isTransactionViewVisible) {
            TransactionSortFields.fromInt(sortFieldId).toString()
        } else {
            AccountSortFields.fromInt(sortFieldId).toString()
        }
        sortByFieldText?.text = text
    }

    private fun setAndSaveTheme() {
        val currTheme = appPreferences.getTheme()
        val newTheme = if (currTheme == AppCompatDelegate.MODE_NIGHT_NO) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO

        AppCompatDelegate.setDefaultNightMode(newTheme)
        appPreferences.setTheme(newTheme)
    }
}
