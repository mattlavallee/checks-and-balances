package io.github.mattlavallee.checksandbalances.core

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Currency
import java.util.Locale

class FormatUtils {
    companion object {
        private var numberFormat: NumberFormat? = null
        private var dateDisplayFormat: SimpleDateFormat? = null

        fun currencyFormat(): NumberFormat {
            if (numberFormat == null) {
                numberFormat = NumberFormat.getCurrencyInstance()
                numberFormat?.maximumFractionDigits = 2
                numberFormat?.currency = Currency.getInstance(Locale.getDefault())
            }

            return numberFormat as NumberFormat
        }

        fun dateFormat(): SimpleDateFormat {
            if (dateDisplayFormat == null) {
                dateDisplayFormat = SimpleDateFormat("MMM dd yyyy", Locale.getDefault())
            }
            return dateDisplayFormat as SimpleDateFormat
        }
    }
}