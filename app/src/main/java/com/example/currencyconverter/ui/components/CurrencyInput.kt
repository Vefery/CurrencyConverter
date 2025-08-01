package com.example.currencyconverter.ui.components

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import kotlin.math.max
import kotlin.math.min
import kotlin.text.format

@Composable
fun CurrencyInput(
    currencyFormatter: (amount: Double, digits: Int?) -> String,
    initialValue: String,
    onDoneInput: (newAmount: String) -> Unit
) {
    val inputState = rememberTextFieldState(initialValue)
    val focusRequester = remember { FocusRequester() }

    val inputTransformation = remember {
        object : InputTransformation {
            override fun TextFieldBuffer.transformInput() {
                // Get the original text before any changes
                val originalText: String = toString()
                val originalCursor: TextRange = selection

                // Filter to only allow digits and single decimal point
                 val filteredText = toString()
                    .filter { it.isDigit() }

                // Convert to decimal value (cents)
                val decimalValue = if (filteredText.isEmpty()) 0.0 else filteredText.toDouble() / 100

                // Format as currency
                val formattedText = currencyFormatter(decimalValue, 2)

                // Update the buffer with formatted text
                replace(0, length, formattedText)

                // Calculate and update selection
                val newCursorPosition = when {
                    originalText.length > formattedText.length -> originalCursor.start - 1
                    originalText.length < formattedText.length -> originalCursor.start + 1
                    else -> originalCursor.start
                }
                selection = TextRange(newCursorPosition)
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BasicTextField(
        modifier = Modifier.focusRequester(focusRequester),
        state = inputState,
        textStyle = MaterialTheme.typography.bodyLarge
            .copy(textAlign = TextAlign.End, textDecoration = TextDecoration.Underline),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        lineLimits = TextFieldLineLimits.SingleLine,
        inputTransformation = inputTransformation,
        onKeyboardAction = { onDoneInput(inputState.text.toString()) }
    )
}