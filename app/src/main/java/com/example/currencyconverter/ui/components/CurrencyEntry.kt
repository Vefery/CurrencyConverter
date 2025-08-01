package com.example.currencyconverter.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.currencyconverter.R
import com.example.currencyconverter.data.dataSource.remote.dto.RateDto

@Composable
fun CurrencyEntry(
    modifier: Modifier = Modifier,
    rate: RateDto,
    currencyName: String,
    balance: Double,
    primary: Boolean = false,
    currencyFormatter: (amount: Double, digits: Int?) -> String,
    balanceFormatter: (amount: Double) -> String,
    onClick: (newCode: String) -> Unit,
    onAmountChange: (newAmount: Double) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var amountText by rememberSaveable { mutableStateOf(currencyFormatter(rate.value, null)) }

    LaunchedEffect(rate.value, isEditing) {
        if (!isEditing) {
            amountText = currencyFormatter(rate.value, null)
        }
    }

    Surface(
        modifier = modifier.clickable(enabled = !primary) {
            onClick(rate.currency)
        },
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SubcomposeAsyncImage(
                modifier = Modifier.padding(5.dp).size(70.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://www.xe.com/svgs/flags/${rate.currency.lowercase()}.static.svg")
                    .decoderFactory(SvgDecoder.Factory())
                    .build(),
                contentDescription = null,
                loading = {
                    CircularProgressIndicator(modifier = Modifier.requiredSize(40.dp), strokeWidth = 2.dp)
                },
                error = {
                    Icon(Icons.Default.Warning, contentDescription = null)
                }
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = rate.currency.uppercase(),
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = currencyName,
                    style = MaterialTheme.typography.bodyMedium
                )
                if (balance > 0.0) {
                    Text(
                        text = "${stringResource(R.string.balance)}: ${balanceFormatter(balance)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            if (isEditing) {
                CurrencyInput(
                    currencyFormatter = currencyFormatter,
                    initialValue = currencyFormatter(rate.value, 2),
                    onDoneInput = {newAmount ->
                        amountText = newAmount
                        isEditing = false
                        val newAmountDouble = newAmount.filter { it.isDigit() }.toDouble()
                        onAmountChange(if (newAmountDouble == 0.0) 1.0 else newAmountDouble)
                    }
                )
            } else {
                Text(
                    text = amountText,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Right,
                    softWrap = false,
                    modifier = Modifier.width(IntrinsicSize.Min).clickable(enabled = primary) { isEditing = true }
                )
            }
            if (primary && rate.value != 1.0) {
                IconButton(
                    modifier = Modifier.size(20.dp),
                    onClick = {
                        onAmountChange(1.0)
                    }
                ) {
                    Icon(Icons.Default.Clear, contentDescription = null)
                }
            }
        }
    }
}