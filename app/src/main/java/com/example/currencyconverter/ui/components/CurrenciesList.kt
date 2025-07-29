package com.example.currencyconverter.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.currencyconverter.data.dataSource.remote.RemoteRatesServiceImpl
import com.example.currencyconverter.data.dataSource.remote.dto.RateDto
import com.example.currencyconverter.domain.logic.CurrencyHelper

@Composable
fun CurrenciesList(
    modifier: Modifier,
    baseCurrency: String,
    amount: Double
) {
    var isLoading by rememberSaveable { mutableStateOf(true) }
    var rates by rememberSaveable { mutableStateOf<List<RateDto>>(emptyList()) }

    LaunchedEffect(Unit) {
        rates = RemoteRatesServiceImpl().getRates(baseCurrencyCode = baseCurrency, amount = amount)
        isLoading = false
    }

    if (isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(modifier = Modifier.requiredSize(100.dp))
        }
    } else {
        LazyColumn(modifier = modifier) {
            items(rates) {
                rate ->
                CurrencyEntry(
                    rate = rate,
                    currencyName = CurrencyHelper.getFullName(rate.currency),
                    balance = 20.0,
                    currencyFormatter = {amount -> CurrencyHelper.formatCurrencyCustom(
                        amount = amount,
                        currencyCode = rate.currency
                    )}
                )
                Spacer(modifier = Modifier.padding(vertical = 2.dp))
            }
        }
    }
}