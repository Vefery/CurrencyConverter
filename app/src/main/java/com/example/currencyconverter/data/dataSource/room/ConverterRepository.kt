package com.example.currencyconverter.data.dataSource.room

import com.example.currencyconverter.data.dataSource.room.account.dao.AccountDao
import com.example.currencyconverter.data.dataSource.room.account.dbo.AccountDbo
import com.example.currencyconverter.data.dataSource.room.transaction.dao.TransactionDao
import com.example.currencyconverter.data.dataSource.room.transaction.dbo.TransactionDbo
import javax.inject.Inject

class ConverterRepository @Inject constructor(
    private val accountDao: AccountDao,
    private val transactionDao: TransactionDao
) {
    fun getAllAccounts() = accountDao.getAllAsFlow()
    suspend fun updateAccounts(data: AccountDbo) = accountDao.insertAll(data)

    suspend fun getAllTransactions() = transactionDao.getAll()
    suspend fun insertTransaction(data: TransactionDbo) = transactionDao.insertAll(data)
}