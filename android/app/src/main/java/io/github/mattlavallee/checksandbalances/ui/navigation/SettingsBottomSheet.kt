package io.github.mattlavallee.checksandbalances.ui.navigation

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentManager
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textview.MaterialTextView
import io.github.mattlavallee.checksandbalances.R
import io.github.mattlavallee.checksandbalances.core.AccountSortFields
import io.github.mattlavallee.checksandbalances.core.Constants
import io.github.mattlavallee.checksandbalances.core.Preferences
import io.github.mattlavallee.checksandbalances.core.TransactionSortFields
import io.github.mattlavallee.checksandbalances.databinding.LayoutSettingsBottomsheetBinding
import io.github.mattlavallee.checksandbalances.ui.transactions.TransactionFragment

class SettingsBottomSheet: BottomSheetDialogFragment() {
    private lateinit var appPreferences: Preferences
    private lateinit var binding: LayoutSettingsBottomsheetBinding
    private var isTransactionViewVisible = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val settingsView = inflater.inflate(R.layout.layout_settings_bottomsheet, container, false)
        binding = LayoutSettingsBottomsheetBinding.bind(settingsView)
        appPreferences = Preferences(requireActivity())

        this.setMenuDisplayText()

        val transFragment = this.setArchiveTransactionState(requireActivity().supportFragmentManager)

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
            } else if (it.itemId == R.id.settings_positive_color) {
                activity?.let { itActivity ->
                    val positiveColor = appPreferences.getPositiveColor()
                    MaterialColorPickerDialog
                        .Builder(itActivity)
                        .setTitle("Positive Color")
                        .setColorShape(ColorShape.CIRCLE)
                        .setColorRes(resources.getIntArray(R.array.color_palette))
                        .setDefaultColor(positiveColor)
                        .setTickColorPerCard(true)
                        .setColorListener { _, colorHex ->
                            appPreferences.setColor(Constants.positiveColorKey, colorHex)
                            this.setPositiveNegativeDisplayName()
                        }
                        .show()
                }
            } else if (it.itemId == R.id.settings_negative_color) {
                activity?.let { itActivity ->
                    val negativeColor = appPreferences.getNegativeColor()
                    MaterialColorPickerDialog
                        .Builder(itActivity)
                        .setTitle("Negative Color")
                        .setColorShape(ColorShape.CIRCLE)
                        .setColorRes(resources.getIntArray(R.array.color_palette))
                        .setDefaultColor(negativeColor)
                        .setTickColorPerCard(true)
                        .setColorListener { _, colorHex ->
                            appPreferences.setColor(Constants.negativeColorKey, colorHex)
                            this.setPositiveNegativeDisplayName()
                        }
                        .show()
                }
            } else if (it.itemId == R.id.settings_archive_transactions) {
                activity?.let {
                    transFragment?.deleteTransactions()
                    super.dismiss()
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

    fun setMenuDisplayText() {
        if (!::binding.isInitialized) {
            return
        }

        this.setSortByDisplayName()
        this.setPositiveNegativeDisplayName()
    }

    private fun setSortByDisplayName() {
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

    private fun setPositiveNegativeDisplayName() {
        val posColor = appPreferences.getPositiveColor()
        val positiveMenuItem = binding.settingsNavigationView.menu.getItem(1)
        val positiveText = positiveMenuItem.actionView?.findViewById<MaterialTextView>(R.id.settings_current_positive_color_preference)
        positiveText?.text = this.getColorSpannable("Sample: $12.34", posColor)

        val negColor = appPreferences.getNegativeColor()
        val negativeMenuItem = binding.settingsNavigationView.menu.getItem(2)
        val negativeText = negativeMenuItem.actionView?.findViewById<MaterialTextView>(R.id.settings_current_negative_color_preference)
        negativeText?.text = this.getColorSpannable("Sample: -$12.34", negColor)
    }

    private fun getColorSpannable(message: String, color: String): SpannableString {
        val str = SpannableString(message)
        str.setSpan((ForegroundColorSpan(Color.parseColor(color))), 8, message.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return str
    }

    private fun setAndSaveTheme() {
        val currTheme = appPreferences.getTheme()
        val newTheme = if (currTheme == AppCompatDelegate.MODE_NIGHT_NO) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO

        AppCompatDelegate.setDefaultNightMode(newTheme)
        appPreferences.setTheme(newTheme)
    }

    private fun setArchiveTransactionState(fragManager: FragmentManager): TransactionFragment? {
        val stackCount = fragManager.backStackEntryCount
        val currStackEntry = if (stackCount > 0) fragManager.getBackStackEntryAt(stackCount - 1) else null

        val isTransactionFragment = currStackEntry?.name == Constants.transactionViewTag
        val deleteTransOpt = binding.settingsNavigationView.menu.getItem(4)
        deleteTransOpt.isEnabled = isTransactionFragment

        val activeFragment = fragManager.findFragmentByTag(currStackEntry?.name)
        return if (activeFragment == null || !isTransactionFragment) null else activeFragment as TransactionFragment
    }
}
