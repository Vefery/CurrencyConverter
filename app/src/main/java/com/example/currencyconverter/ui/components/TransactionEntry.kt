package com.example.currencyconverter.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.currencyconverter.data.dataSource.room.transaction.dbo.TransactionDbo
import com.example.currencyconverter.domain.logic.CurrencyHelper
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TransactionEntry(
    modifier: Modifier = Modifier,
    transaction: TransactionDbo
) {
    val context = LocalContext.current
    val datePattern = if (LocalConfiguration.current.locales[0] == Locale.ENGLISH) "MM/dd/yyyy hh:mm a" else "dd/MM/yyyy HH:mm"


    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(5.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = transaction.from,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = CurrencyHelper.formatCurrency(transaction.fromAmount, transaction.from, context = context),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.padding(10.dp))
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.weight(1f))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = transaction.to,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = CurrencyHelper.formatCurrency(transaction.toAmount, transaction.to, context = context),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Text(
                modifier = Modifier.padding(5.dp),
                text = transaction.dateTime.format(DateTimeFormatter.ofPattern(datePattern)),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}