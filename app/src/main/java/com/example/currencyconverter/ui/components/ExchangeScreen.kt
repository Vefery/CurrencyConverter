package com.example.currencyconverter.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencyconverter.data.dataSource.remote.RemoteRatesServiceImpl
import com.example.currencyconverter.domain.entity.Exchange
import com.example.currencyconverter.domain.logic.AccountViewModel
import com.example.currencyconverter.domain.logic.CurrencyHelper

@Composable
fun ExchangeScreen(
    modifier: Modifier,
    curExchange: Exchange,
    balanceMap: Map<String, Double>,
    viewModel: AccountViewModel = hiltViewModel(),
) {
    val sourceCurrencyName: String = rememberSaveable { CurrencyHelper.getFullName(curExchange.sourceRate.currency) }
    val targetCurrencyName: String = rememberSaveable { CurrencyHelper.getFullName(curExchange.targetRate.currency) }
    var rateValue: Double by rememberSaveable { mutableDoubleStateOf(1.0) }
    var isLoading by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isLoading = true
        rateValue = RemoteRatesServiceImpl()
            .getRates(baseCurrencyCode = curExchange.targetRate.currency, amount = 1.0)
            .find { it.currency == curExchange.sourceRate.currency }!!
            .value
        isLoading = false
    }

    if (isLoading) {
        LoadingCircle(
            modifier = modifier
        )
    } else {
        Column(
            modifier = modifier
        ) {
            Text(
                modifier = Modifier
                    .padding(10.dp)
                    .padding(top = 10.dp),
                text = "$sourceCurrencyName to $targetCurrencyName",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                modifier = Modifier.padding(vertical = 5.dp, horizontal = 15.dp),
                text = "${CurrencyHelper.formatCurrency(amount = 1.0, currencyCode = curExchange.targetRate.currency)} = ${CurrencyHelper.formatCurrency(amount = rateValue, currencyCode = curExchange.sourceRate.currency)}",
                style = MaterialTheme.typography.labelMedium,
            )
            CurrencyEntry(
                modifier = Modifier.padding(bottom = 5.dp).clickable(
                    enabled = false,
                    onClick = {}
                ),
                rate = curExchange.targetRate,
                currencyName = targetCurrencyName,
                balance = balanceMap.getOrDefault(key = curExchange.targetRate.currency.uppercase(), defaultValue = 0.0),
                currencyFormatter = {amount -> CurrencyHelper.formatCurrencySign(
                    amount = amount,
                    currencyCode = curExchange.targetRate.currency,
                    positive = true
                )},
                balanceFormatter = {amount -> CurrencyHelper.formatCurrency(
                    amount = amount,
                    currencyCode = curExchange.targetRate.currency
                )},
                onClick = {},
                onAmountChange = {}
            )
            CurrencyEntry(
                modifier = Modifier.padding(bottom = 20.dp).clickable(
                    enabled = false,
                    onClick = {}
                ),
                rate = curExchange.sourceRate,
                currencyName = sourceCurrencyName,
                balance = balanceMap.getOrDefault(key = curExchange.sourceRate.currency.uppercase(), defaultValue = 0.0),
                currencyFormatter = {amount -> CurrencyHelper.formatCurrencySign(
                    amount = amount,
                    currencyCode = curExchange.sourceRate.currency,
                    positive = false
                )},
                balanceFormatter = {amount -> CurrencyHelper.formatCurrency(
                    amount = amount,
                    currencyCode = curExchange.sourceRate.currency
                )},
                onClick = {},
                onAmountChange = {}
            )
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    modifier = Modifier.padding(20.dp),
                    shape = MaterialTheme.shapes.small,
                    onClick = {}
                ) {
                    Text(
                        modifier = Modifier.padding(20.dp),
                        text = "Buy $targetCurrencyName for $sourceCurrencyName"
                    )
                }
            }
        }
    }

}