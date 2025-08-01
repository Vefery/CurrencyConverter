package com.example.currencyconverter.domain.logic

import android.content.Context
import com.example.currencyconverter.R
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Currency
import java.util.Locale

object CurrencyHelper {
    private val currencySymbols: Map<String, Int> = mapOf(
        "EUR" to R.string.symbol_eur,
        "AUD" to R.string.symbol_aud,
        "BGN" to R.string.symbol_bgn,
        "BRL" to R.string.symbol_brl,
        "CAD" to R.string.symbol_cad,
        "CHF" to R.string.symbol_chf,
        "CNY" to R.string.symbol_cny,
        "CZK" to R.string.symbol_czk,
        "DKK" to R.string.symbol_dkk,
        "GBP" to R.string.symbol_gbp,
        "HKD" to R.string.symbol_hkd,
        "HRK" to R.string.symbol_hrk,
        "HUF" to R.string.symbol_huf,
        "IDR" to R.string.symbol_idr,
        "ILS" to R.string.symbol_ils,
        "INR" to R.string.symbol_inr,
        "ISK" to R.string.symbol_isk,
        "JPY" to R.string.symbol_jpy,
        "KRW" to R.string.symbol_krw,
        "MXN" to R.string.symbol_mxn,
        "MYR" to R.string.symbol_myr,
        "NOK" to R.string.symbol_nok,
        "NZD" to R.string.symbol_nzd,
        "PHP" to R.string.symbol_php,
        "PLN" to R.string.symbol_pln,
        "RON" to R.string.symbol_ron,
        "RUB" to R.string.symbol_rub,
        "SEK" to R.string.symbol_sek,
        "SGD" to R.string.symbol_sgd,
        "THB" to R.string.symbol_thb,
        "TRY" to R.string.symbol_try,
        "USD" to R.string.symbol_usd,
        "ZAR" to R.string.symbol_zar,
    )

    private val currencyNames: Map<String, Int> = mapOf(
        "EUR" to R.string.currency_eur,
        "AUD" to R.string.currency_aud,
        "BGN" to R.string.currency_bgn,
        "BRL" to R.string.currency_brl,
        "CAD" to R.string.currency_cad,
        "CHF" to R.string.currency_chf,
        "CNY" to R.string.currency_cny,
        "CZK" to R.string.currency_czk,
        "DKK" to R.string.currency_dkk,
        "GBP" to R.string.currency_gbp,
        "HKD" to R.string.currency_hkd,
        "HRK" to R.string.currency_hrk,
        "HUF" to R.string.currency_huf,
        "IDR" to R.string.currency_idr,
        "ILS" to R.string.currency_ils,
        "INR" to R.string.currency_inr,
        "ISK" to R.string.currency_isk,
        "JPY" to R.string.currency_jpy,
        "KRW" to R.string.currency_krw,
        "MXN" to R.string.currency_mxn,
        "MYR" to R.string.currency_myr,
        "NOK" to R.string.currency_nok,
        "NZD" to R.string.currency_nzd,
        "PHP" to R.string.currency_php,
        "PLN" to R.string.currency_pln,
        "RON" to R.string.currency_ron,
        "RUB" to R.string.currency_rub,
        "SEK" to R.string.currency_sek,
        "SGD" to R.string.currency_sgd,
        "THB" to R.string.currency_thb,
        "TRY" to R.string.currency_try,
        "USD" to R.string.currency_usd,
        "ZAR" to R.string.currency_zar,
    )

    fun getFullNameResId(code: String): Int {
        return currencyNames[code.uppercase()]!!
    }
    fun formatCurrency(
        amount: Double,
        currencyCode: String,
        fractionDigits: Int? = null,
        context: Context
    ): String {
        val symbols = DecimalFormatSymbols(Locale.ROOT).apply {
            groupingSeparator = '.'
            decimalSeparator = ','
            monetaryDecimalSeparator = ','
        }

        val pattern: String = if (fractionDigits != null) {
            "#,##0.${"0".repeat(fractionDigits)}"
        } else {
            if (amount % 1.0 == 0.0) {
                "#,###"
            } else {
                "#,##0.${"0".repeat(if (amount < 100) 4 else 2)}"
            }
        }
        val formatter = DecimalFormat(pattern, symbols)

        return "${context.getString(currencySymbols[currencyCode.uppercase()]!!)}${formatter.format(amount)}"
    }
    fun formatCurrencySign(
        amount: Double,
        currencyCode: String,
        positive: Boolean,
        fractionDigits: Int? = null,
        context: Context
    ): String {
        val symbols = DecimalFormatSymbols(Locale.ROOT).apply {
            groupingSeparator = '.'
            decimalSeparator = ','
            monetaryDecimalSeparator = ','
        }
        val pattern: String = if (fractionDigits != null) {
            "#,##0.${"0".repeat(fractionDigits)}"
        } else {
            if (amount % 1.0 == 0.0) {
                "#,###"
            } else {
                "#,##0.${"0".repeat(if (amount < 100) 4 else 2)}"
            }
        }
        val formatter = DecimalFormat(pattern, symbols)
        val sign = if (positive) '+' else '-'

        return "$sign\u2060${context.getString(currencySymbols[currencyCode.uppercase()]!!)}${formatter.format(amount)}"
    }
}