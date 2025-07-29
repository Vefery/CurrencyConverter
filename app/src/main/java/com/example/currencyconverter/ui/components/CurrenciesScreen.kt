package com.example.currencyconverter.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.currencyconverter.domain.logic.AccountViewModel

@Composable
fun CurrenciesScreen(
    modifier: Modifier
) {
    var currentCurrency by rememberSaveable { mutableStateOf("USD") }
    var currentAmount by rememberSaveable { mutableDoubleStateOf(1.0) }

    CurrenciesList(
        modifier = modifier,
        baseCurrency = currentCurrency,
        amount = currentAmount,
        onCurrencyClick = {
            newCode -> currentCurrency = newCode
        }
    )
}