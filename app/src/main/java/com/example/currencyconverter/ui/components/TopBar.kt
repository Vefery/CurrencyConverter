package com.example.currencyconverter.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onDrawerClick: () -> Unit
) {
    TopAppBar(
        title = { Text(text = "Menu") },
        navigationIcon = {
            IconButton(
                onClick = onDrawerClick,
            ) {
                Icon(Icons.Default.Menu, contentDescription = null)
            }
        }
    )
}