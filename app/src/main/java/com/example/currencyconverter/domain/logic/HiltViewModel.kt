package com.example.currencyconverter.domain.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.data.dataSource.room.ConverterRepository
import com.example.currencyconverter.data.dataSource.room.account.dbo.AccountDbo
import com.example.currencyconverter.data.dataSource.room.transaction.dbo.TransactionDbo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repository: ConverterRepository
) : ViewModel() {
    fun getBalanceMap() = repository.getAllAccountsFlow().map { accounts -> accounts.associate { it.code to it.amount } }

    suspend fun updateBalance(data: TransactionDbo) {
        val balance = repository.getAllAccounts().associate { it.code to it.amount }
        insertTransaction(data)
        repository.updateAccounts(AccountDbo(
            code = data.from,
            amount = balance[data.from]!! - data.fromAmount
        ))
        repository.updateAccounts(AccountDbo(
            code = data.to,
            amount = balance.getOrDefault(key = data.to, defaultValue = 0.0) + data.toAmount
        ))
    }

    suspend fun getTransactions() = repository.getAllTransactions()
    suspend fun insertTransaction(data: TransactionDbo) = repository.insertTransaction(data)
}