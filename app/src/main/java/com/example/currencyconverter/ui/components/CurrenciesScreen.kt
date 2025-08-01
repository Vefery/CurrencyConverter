package com.example.currencyconverter.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.currencyconverter.data.dataSource.remote.RemoteRatesServiceImpl
import com.example.currencyconverter.data.dataSource.remote.dto.RateDto
import com.example.currencyconverter.domain.entity.Exchange
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun CurrenciesScreen(
    modifier: Modifier,
    onExchange: (newExchange: Exchange) -> Unit
) {
    var currentCurrency by rememberSaveable { mutableStateOf("USD") }
    var currentAmount by rememberSaveable { mutableDoubleStateOf(1.0) }
    var isLoading by rememberSaveable { mutableStateOf(true) }
    var rates by rememberSaveable { mutableStateOf<List<RateDto>>(emptyList()) }
    val remoteRatesServiceImpl = RemoteRatesServiceImpl()

    val currencyClickFun = if (currentAmount != 1.0) {
        { newCode: String ->
            onExchange(Exchange(
                targetRate = RateDto(
                    currency = currentCurrency,
                    value = currentAmount
                ),
                sourceRate = RateDto(
                    currency = newCode,
                    value = rates.find { it.currency == newCode }!!.value
                )
            ))
        }
    } else {
        { newCode: String -> currentCurrency = newCode }
    }

    LaunchedEffect(currentCurrency, currentAmount) {
        isLoading = true
        while(isActive) {
            delay(1000L)
            rates = remoteRatesServiceImpl.getRates(baseCurrencyCode = currentCurrency, amount = currentAmount).toList()
            isLoading = false
        }
    }

    CurrenciesList(
        modifier = modifier,
        amount = currentAmount,
        onCurrencyClick = currencyClickFun,
        isLoading = isLoading,
        rates = rates,
        onAmountChange = {newAmount ->
            currentAmount = newAmount
        }
    )
}