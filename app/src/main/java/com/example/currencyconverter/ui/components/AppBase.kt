package com.example.currencyconverter.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.currencyconverter.data.dataSource.room.account.dbo.AccountDbo
import com.example.currencyconverter.domain.entity.Exchange
import com.example.currencyconverter.domain.entity.ExchangeNavType
import com.example.currencyconverter.domain.logic.AccountViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
object CurrenciesNav
@Serializable
data class ExchangeNav(val exchange: Exchange)
@Serializable
object TransactionsNav

@Composable
fun AppBase(
    viewModel: AccountViewModel = hiltViewModel(),
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                NavigationDrawerItem(
                    shape = MaterialTheme.shapes.small,
                    label = { Text(text = "Currencies") },
                    selected = false,
                    onClick = {
                        navController.navigate(route = CurrenciesNav)
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
                    selected = false,
                    onClick = {
                        navController.navigate(route = TransactionsNav)
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
            NavHost(
                navController = navController,
                startDestination = CurrenciesNav,
                enterTransition = {
                    EnterTransition.None
                },
                exitTransition = {
                    ExitTransition.None
                }) {
                composable<CurrenciesNav> {
                    CurrenciesScreen(
                        modifier = Modifier.padding(innerPadding),
                        onExchange = {newExchange ->
                            navController.navigate(route = ExchangeNav(newExchange))
                        }
                    )
                }
                composable<ExchangeNav>(
                    typeMap = mapOf(
                        typeOf<Exchange>() to ExchangeNavType
                    )
                ) { backStackEntry ->
                    val exchange = backStackEntry.toRoute<ExchangeNav>()
                    BackHandler(true) { }
                    ExchangeScreen(
                        modifier = Modifier.padding(innerPadding),
                        curExchange = exchange.exchange,
                        handleExchange = { transaction ->
                            coroutineScope.launch {
                                viewModel.updateBalance(transaction)

                                navController.popBackStack()
                                navController.navigate(route = CurrenciesNav)
                            }
                        }
                    )
                }
                composable<TransactionsNav> {
                    TransactionsScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}