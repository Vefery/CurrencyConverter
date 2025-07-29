package com.example.currencyconverter.data.dataSource.room

import com.example.currencyconverter.data.dataSource.room.account.dao.AccountDao
import javax.inject.Inject

class ConverterRepository @Inject constructor(private val accountDao: AccountDao) {
    suspend fun getAllAccounts() = accountDao.getAll()
}