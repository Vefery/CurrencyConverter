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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencyconverter.data.dataSource.remote.RemoteRatesServiceImpl
import com.example.currencyconverter.data.dataSource.remote.dto.RateDto
import com.example.currencyconverter.data.dataSource.room.ConverterDatabase
import com.example.currencyconverter.data.dataSource.room.ConverterDatabase_Impl
import com.example.currencyconverter.data.dataSource.room.account.dao.AccountDao
import com.example.currencyconverter.data.dataSource.room.account.dao.AccountDao_Impl
import com.example.currencyconverter.data.dataSource.room.account.dbo.AccountDbo
import com.example.currencyconverter.domain.logic.AccountViewModel
import com.example.currencyconverter.domain.logic.CurrencyHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.time.delay


@Composable
fun CurrenciesList(
    viewModel: AccountViewModel = hiltViewModel(),
    modifier: Modifier,
    baseCurrency: String,
    amount: Double,
    onCurrencyClick: (newCode: String) -> Unit,
    onAmountChange: (newAmount: Double) -> Unit
) {
    var isLoading by rememberSaveable { mutableStateOf(true) }
    var rates by rememberSaveable { mutableStateOf<List<RateDto>>(emptyList()) }
    var balanceMap: Map<String, Double> by rememberSaveable { mutableStateOf(emptyMap()) }

    LaunchedEffect(baseCurrency, amount) {
        balanceMap = viewModel.getAccounts().associate { it.code to it.amount }
        while(true) {
            delay(1000L)
            rates = RemoteRatesServiceImpl().getRates(baseCurrencyCode = baseCurrency, amount = amount)
            isLoading = false
        }
    }

    if (isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(modifier = Modifier.requiredSize(100.dp))
        }
    } else {
        val ratesFiltered: List<RateDto> = if (amount != 1.0) {
            rates.subList(1, rates.size).filter { it.value < balanceMap.getOrDefault(key = it.currency.uppercase(), defaultValue = 0.0) }
        } else {
            rates.subList(1, rates.size)
        }

        LazyColumn(modifier = modifier) {
            item {
                CurrencyEntry(
                    rate = rates[0],
                    currencyName = CurrencyHelper.getFullName(rates[0].currency),
                    balance = balanceMap.getOrDefault(key = rates[0].currency.uppercase(), defaultValue = 0.0),
                    currencyFormatter = {amount -> CurrencyHelper.formatCurrencyCustom(
                        amount = amount,
                        currencyCode = rates[0].currency
                    )},
                    onClick = {
                        isLoading = true
                        onCurrencyClick(rates[0].currency)
                    },
                    onAmountChange = {newAmount ->
                        onAmountChange(newAmount)
                        isLoading = true
                    },
                    primary = true
                )
                Spacer(modifier = Modifier.padding(vertical = 2.dp))
            }
            items(ratesFiltered) {
                rate ->
                CurrencyEntry(
                    rate = rate,
                    currencyName = CurrencyHelper.getFullName(rate.currency),
                    balance = balanceMap.getOrDefault(key = rate.currency.uppercase(), defaultValue = 0.0),
                    currencyFormatter = {amount -> CurrencyHelper.formatCurrencyCustom(
                        amount = amount,
                        currencyCode = rate.currency
                    )},
                    onClick = {
                        isLoading = true
                        onCurrencyClick(rate.currency)
                    },
                    onAmountChange = {}
                )
                Spacer(modifier = Modifier.padding(vertical = 2.dp))
            }
        }
    }
}