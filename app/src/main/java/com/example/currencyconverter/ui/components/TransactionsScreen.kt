package com.example.currencyconverter.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencyconverter.data.dataSource.room.transaction.dbo.TransactionDbo
import com.example.currencyconverter.domain.logic.AccountViewModel

@Composable
fun TransactionsScreen(
    modifier: Modifier,
    viewModel: AccountViewModel = hiltViewModel(),
) {
    var isLoading by rememberSaveable { mutableStateOf(true) }
    var transactions by rememberSaveable { mutableStateOf<List<TransactionDbo>>(emptyList()) }

    LaunchedEffect(Unit) {
        isLoading = true
        transactions = viewModel.getTransactions()
        isLoading = false
    }

    if (isLoading) {
        LoadingCircle(
            modifier = modifier
        )
    } else {
        LazyColumn(
            modifier = modifier
        ) {
            items(transactions.reversed()) {
                transaction ->
                TransactionEntry(
                    transaction = transaction
                )
                Spacer(modifier = Modifier.padding(vertical = 2.dp))
            }
        }
    }
}