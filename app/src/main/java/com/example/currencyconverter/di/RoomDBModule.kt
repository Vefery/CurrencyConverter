package com.example.currencyconverter.di

import android.content.Context
import androidx.room.Room
import com.example.currencyconverter.data.dataSource.room.ConverterDatabase
import com.example.currencyconverter.data.dataSource.room.account.dao.AccountDao
import com.example.currencyconverter.data.dataSource.room.transaction.dao.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DBModule {
    @Singleton
    @Provides
    fun provideConverterDB(@ApplicationContext context: Context): ConverterDatabase {
        return Room.databaseBuilder(
            context,
            ConverterDatabase::class.java,
            "converter.db"
        )
            .createFromAsset("databases/converter.db")
            .build()
    }
    @Singleton
    @Provides
    fun provideAccountsDao(db: ConverterDatabase): AccountDao =
        db.accountDao()

    @Singleton
    @Provides
    fun provideTransactionsDao(db: ConverterDatabase): TransactionDao =
        db.transactionDao()
}