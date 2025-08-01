package com.example.currencyconverter.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencyconverter.data.dataSource.remote.dto.RateDto
import com.example.currencyconverter.domain.logic.AccountViewModel
import com.example.currencyconverter.domain.logic.CurrencyHelper

@Composable
fun CurrenciesList(
    viewModel: AccountViewModel = hiltViewModel(),
    modifier: Modifier,
    amount: Double,
    isLoading: Boolean,
    rates: List<RateDto>,
    onCurrencyClick: (newCode: String) -> Unit,
    onAmountChange: (newAmount: Double) -> Unit
) {
    val balanceMap by viewModel.getBalanceMap().collectAsState(initial = emptyMap())
    val context = LocalContext.current

    if (isLoading) {
        LoadingCircle(
            modifier = modifier
        )
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
                    currencyName = stringResource(CurrencyHelper.getFullNameResId(rates[0].currency)),
                    balance = balanceMap.getOrDefault(key = rates[0].currency.uppercase(), defaultValue = 0.0),
                    currencyFormatter = {amount, digits -> CurrencyHelper.formatCurrency(
                        amount = amount,
                        currencyCode = rates[0].currency,
                        fractionDigits = digits,
                        context = context
                    )},
                    balanceFormatter = {amount -> CurrencyHelper.formatCurrency(
                        amount = amount,
                        currencyCode = rates[0].currency,
                        context = context
                    )},
                    onClick = {
                        onCurrencyClick(rates[0].currency)
                    },
                    onAmountChange = {newAmount ->
                        onAmountChange(newAmount)
                    },
                    primary = true
                )
                Spacer(modifier = Modifier.padding(vertical = 2.dp))
            }
            items(ratesFiltered) {
                rate ->
                CurrencyEntry(
                    rate = rate,
                    currencyName = stringResource(CurrencyHelper.getFullNameResId(rate.currency)),
                    balance = balanceMap.getOrDefault(key = rate.currency.uppercase(), defaultValue = 0.0),
                    currencyFormatter = {amount, digits -> CurrencyHelper.formatCurrency(
                        amount = amount,
                        currencyCode = rate.currency,
                        fractionDigits = digits,
                        context = context
                    )},
                    balanceFormatter = {amount -> CurrencyHelper.formatCurrency(
                        amount = amount,
                        currencyCode = rate.currency,
                        context = context
                    )},
                    onClick = onCurrencyClick,
                    onAmountChange = {},
                )
                Spacer(modifier = Modifier.padding(vertical = 2.dp))
            }
        }
    }
}