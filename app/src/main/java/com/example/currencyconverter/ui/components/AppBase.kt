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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.currencyconverter.data.dataSource.remote.RemoteRatesServiceImpl
import com.example.currencyconverter.data.dataSource.remote.dto.RateDto
import com.example.currencyconverter.domain.logic.AccountViewModel
import com.example.currencyconverter.domain.logic.CurrencyHelper
import kotlinx.coroutines.launch

enum class CurrentScreen {
    Currencies,
    Exchange,
    Transactions
}

@Composable
fun AppBase() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var curScreen by rememberSaveable { mutableStateOf(CurrentScreen.Currencies) }

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
                    modifier = Modifier.padding(innerPadding)
                )
                CurrentScreen.Exchange -> {}
                CurrentScreen.Transactions -> {}
            }
        }
    }
}