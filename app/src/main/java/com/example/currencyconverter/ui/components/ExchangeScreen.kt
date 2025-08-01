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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencyconverter.R
import com.example.currencyconverter.data.dataSource.room.transaction.dbo.TransactionDbo
import com.example.currencyconverter.domain.entity.Exchange
import com.example.currencyconverter.domain.logic.AccountViewModel
import com.example.currencyconverter.domain.logic.CurrencyHelper
import java.time.LocalDateTime

@Composable
fun ExchangeScreen(
    viewModel: AccountViewModel = hiltViewModel(),
    modifier: Modifier,
    curExchange: Exchange,
    handleExchange: (transaction: TransactionDbo) -> Unit
) {
    var isButtonActive by rememberSaveable { mutableStateOf(true) }
    val balanceMap by viewModel.getBalanceMap().collectAsState(initial = emptyMap())
    val context = LocalContext.current

    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier
                .padding(10.dp)
                .padding(top = 10.dp),
            text = stringResource(R.string.exchange_to, stringResource(CurrencyHelper.getFullNameResId(curExchange.sourceRate.currency) ), stringResource(CurrencyHelper.getFullNameResId(curExchange.targetRate.currency) )),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            modifier = Modifier.padding(vertical = 5.dp, horizontal = 15.dp),
            text = "${CurrencyHelper.formatCurrency(amount = 1.0, currencyCode = curExchange.targetRate.currency, context = context)} = ${CurrencyHelper.formatCurrency(amount = curExchange.sourceRate.value / curExchange.targetRate.value, currencyCode = curExchange.sourceRate.currency, context = context)}",
            style = MaterialTheme.typography.labelMedium,
        )
        CurrencyEntry(
            modifier = Modifier.padding(bottom = 5.dp).clickable(
                enabled = false,
                onClick = {}
            ),
            rate = curExchange.targetRate,
            currencyName = stringResource(CurrencyHelper.getFullNameResId(curExchange.targetRate.currency) ),
            balance = balanceMap.getOrDefault(key = curExchange.targetRate.currency.uppercase(), defaultValue = 0.0),
            currencyFormatter = {amount, digits -> CurrencyHelper.formatCurrencySign(
                amount = amount,
                currencyCode = curExchange.targetRate.currency,
                positive = true,
                fractionDigits = digits,
                context = context
            )},
            balanceFormatter = {amount -> CurrencyHelper.formatCurrency(
                amount = amount,
                currencyCode = curExchange.targetRate.currency,
                context = context
            )},
            onClick = {},
            onAmountChange = {}
        )
        CurrencyEntry(
            modifier = Modifier.padding(bottom = 20.dp),
            rate = curExchange.sourceRate,
            currencyName = stringResource(CurrencyHelper.getFullNameResId(curExchange.sourceRate.currency) ),
            balance = balanceMap.getOrDefault(key = curExchange.sourceRate.currency.uppercase(), defaultValue = 0.0),
            currencyFormatter = {amount, digits -> CurrencyHelper.formatCurrencySign(
                amount = amount,
                currencyCode = curExchange.sourceRate.currency,
                positive = false,
                fractionDigits = digits,
                context = context
            )},
            balanceFormatter = {amount -> CurrencyHelper.formatCurrency(
                amount = amount,
                currencyCode = curExchange.sourceRate.currency,
                context = context
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
                enabled = isButtonActive,
                onClick = {
                        handleExchange(TransactionDbo(
                            from = curExchange.sourceRate.currency,
                            to = curExchange.targetRate.currency,
                            fromAmount = curExchange.sourceRate.value,
                            toAmount = curExchange.targetRate.value,
                            dateTime = LocalDateTime.now(),
                            id = 0
                        ))
                }
            ) {
                Text(
                    modifier = Modifier.padding(20.dp),
                    text = stringResource(R.string.buy_for, stringResource(CurrencyHelper.getFullNameResId(curExchange.targetRate.currency) ), stringResource(CurrencyHelper.getFullNameResId(curExchange.sourceRate.currency) ))
                )
            }
        }
    }
}