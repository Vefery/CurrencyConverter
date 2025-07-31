package com.example.currencyconverter.domain.logic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.data.dataSource.room.ConverterRepository
import com.example.currencyconverter.data.dataSource.room.account.dao.AccountDao
import com.example.currencyconverter.data.dataSource.room.account.dbo.AccountDbo
import com.example.currencyconverter.data.dataSource.room.transaction.dbo.TransactionDbo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repository: ConverterRepository
) : ViewModel() {
    suspend fun getAccounts() = repository.getAllAccounts()
    suspend fun updateAccount(data: AccountDbo) = repository.updateAccounts(data)

    suspend fun getTransactions() = repository.getAllTransactions()
    suspend fun insertTransaction(data: TransactionDbo) = repository.insertTransaction(data)
}