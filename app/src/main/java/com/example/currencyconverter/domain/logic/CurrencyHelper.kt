package com.example.currencyconverter.domain.logic

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Currency
import java.util.Locale

object CurrencyHelper {
    fun getFullName(code: String): String {
        val currency = Currency.getInstance(code.uppercase())
        return currency.getDisplayName(Locale.ENGLISH)
    }
    fun formatCurrency(
        amount: Double,
        currencyCode: String,
        fractionDigits: Int? = null
    ): String {
        val currency = Currency.getInstance(currencyCode.uppercase())
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

        return "${currency.symbol}${formatter.format(amount)}"
    }
    fun formatCurrencySign(
        amount: Double,
        currencyCode: String,
        positive: Boolean,
        fractionDigits: Int? = null
    ): String {
        val currency = Currency.getInstance(currencyCode.uppercase())
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

        return "$sign\u2060${currency.symbol}${formatter.format(amount)}"
    }
}