package com.example.currencyconverter.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.currencyconverter.domain.logic.AccountViewModel
import com.example.currencyconverter.ui.components.CurrenciesList

@Composable
fun CurrenciesScreen(
    modifier: Modifier
) {
    var currentCurrency by rememberSaveable { mutableStateOf("USD") }
    var currentAmount by rememberSaveable { mutableDoubleStateOf(1.0) }
    val currencyClickFun = if (currentAmount != 1.0) {
        {  }
    } else {
        { newCode: String -> currentCurrency = newCode }
    }

    CurrenciesList(
        modifier = modifier,
        baseCurrency = currentCurrency,
        amount = currentAmount,
        onCurrencyClick = currencyClickFun,
        onAmountChange = {newAmount ->
            currentAmount = newAmount
        }
    )
}