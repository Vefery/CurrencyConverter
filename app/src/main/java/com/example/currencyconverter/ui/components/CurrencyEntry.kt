package com.example.currencyconverter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.currencyconverter.data.dataSource.remote.dto.RateDto
import com.example.currencyconverter.domain.logic.CurrencyHelper

@Composable
fun CurrencyEntry(
    modifier: Modifier = Modifier,
    rate: RateDto,
    currencyName: String,
    balance: Double,
    currencyFormatter: (amount: Double) -> String
) {
    Surface(
        modifier = modifier,
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
                Text(
                    text = "Balance: ${currencyFormatter(balance)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = currencyFormatter(rate.value),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}