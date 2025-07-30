package com.example.currencyconverter.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencyconverter.data.dataSource.remote.RemoteRatesServiceImpl
import com.example.currencyconverter.data.dataSource.remote.dto.RateDto
import com.example.currencyconverter.domain.entity.Exchange
import com.example.currencyconverter.domain.logic.AccountViewModel
import com.example.currencyconverter.domain.logic.CurrencyHelper
import kotlinx.coroutines.launch

enum class CurrentScreen {
    Currencies,
    Exchange,
    Transactions
}

@Composable
fun AppBase(
    viewModel: AccountViewModel = hiltViewModel(),
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var curScreen by rememberSaveable { mutableStateOf(CurrentScreen.Currencies) }
    var currentExchange by rememberSaveable { mutableStateOf<Exchange?>(null) }
    var balanceMap: Map<String, Double> by rememberSaveable { mutableStateOf(emptyMap()) }

    LaunchedEffect(Unit) {
        balanceMap = viewModel.getAccounts().associate { it.code to it.amount }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                NavigationDrawerItem(
                    shape = MaterialTheme.shapes.small,
                    label = { Text(text = "Currencies") },
                    selected = curScreen == CurrentScreen.Currencies,
                    onClick = {
                        curScreen = CurrentScreen.Currencies
                        scope.launch {
                            drawerState.apply {
                                close()
                            }
                        }
                    }
                )
                NavigationDrawerItem(
                    shape = MaterialTheme.shapes.small,
                    label = { Text(text = "Transactions") },
                    selected = curScreen == CurrentScreen.Transactions,
                    onClick = {
                        curScreen = CurrentScreen.Transactions
                        scope.launch {
                            drawerState.apply {
                                close()
                            }
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = { TopBar(
                onDrawerClick = {
                    scope.launch {
                        drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                }
            ) }
        ) {
            innerPadding ->
            when(curScreen) {
                CurrentScreen.Currencies -> CurrenciesScreen(
                    modifier = Modifier.padding(innerPadding),
                    onExchange = {newExchange ->
                        currentExchange = newExchange
                        curScreen = CurrentScreen.Exchange
                    },
                    balanceMap = balanceMap
                )
                CurrentScreen.Exchange -> ExchangeScreen(
                    modifier = Modifier.padding(innerPadding),
                    curExchange = currentExchange!!,
                    balanceMap = balanceMap
                )
                CurrentScreen.Transactions -> {}
            }
        }
    }
}