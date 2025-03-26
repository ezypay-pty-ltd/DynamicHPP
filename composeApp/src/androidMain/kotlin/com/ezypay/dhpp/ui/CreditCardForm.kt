package com.ezypay.dhpp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ezypay.dhpp.viewmodel.CreditCardViewModel

@Composable
fun CreditCardFormScreen(viewModel: CreditCardViewModel) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()
    
    LaunchedEffect(state.isSubmissionSuccessful, state.generalError) {
        if (state.isSubmissionSuccessful) {
            snackbarHostState.showSnackbar("Payment processed successfully")
            viewModel.resetSubmissionState()
        } else if (state.generalError.isNotEmpty()) {
            snackbarHostState.showSnackbar(state.generalError)
            viewModel.resetSubmissionState()
        }
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Payment Details",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        CreditCardForm(
                            cardNumber = state.creditCard.number,
                            cardholderName = state.creditCard.holderName,
                            expiryMonth = state.creditCard.expiryMonth,
                            expiryYear = state.creditCard.expiryYear,
                            cvv = state.creditCard.cvv,
                            numberError = state.numberError,
                            nameError = state.nameError,
                            expiryError = state.expiryError,
                            cvvError = state.cvvError,
                            onCardNumberChanged = viewModel::updateCardNumber,
                            onCardholderNameChanged = viewModel::updateCardholderName,
                            onExpiryMonthChanged = viewModel::updateExpiryMonth,
                            onExpiryYearChanged = viewModel::updateExpiryYear,
                            onCvvChanged = viewModel::updateCvv,
                            isSubmitting = state.isSubmitting,
                            onSubmit = viewModel::processPayment
                        )
                    }
                }
            }
            
            if (state.isSubmitting) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun CreditCardForm(
    cardNumber: String,
    cardholderName: String,
    expiryMonth: String,
    expiryYear: String,
    cvv: String,
    numberError: String,
    nameError: String,
    expiryError: String,
    cvvError: String,
    onCardNumberChanged: (String) -> Unit,
    onCardholderNameChanged: (String) -> Unit,
    onExpiryMonthChanged: (String) -> Unit,
    onExpiryYearChanged: (String) -> Unit,
    onCvvChanged: (String) -> Unit,
    isSubmitting: Boolean,
    onSubmit: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Card Number
        OutlinedTextField(
            value = cardNumber,
            onValueChange = onCardNumberChanged,
            label = { Text("Card Number") },
            leadingIcon = { Icon(Icons.Default.CreditCard, contentDescription = "Card") },
            modifier = Modifier.fillMaxWidth(),
            isError = numberError.isNotEmpty(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            singleLine = true
        )
        
        if (numberError.isNotEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(Icons.Default.Error, contentDescription = "Error", modifier = Modifier.padding(end = 4.dp))
                Text(numberError, color = MaterialTheme.colors.error)
            }
        }
        
        // Cardholder Name
        OutlinedTextField(
            value = cardholderName,
            onValueChange = onCardholderNameChanged,
            label = { Text("Cardholder Name") },
            modifier = Modifier.fillMaxWidth(),
            isError = nameError.isNotEmpty(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            singleLine = true
        )
        
        if (nameError.isNotEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(Icons.Default.Error, contentDescription = "Error", modifier = Modifier.padding(end = 4.dp))
                Text(nameError, color = MaterialTheme.colors.error)
            }
        }
        
        // Expiry Date and CVV
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Expiry Date (MM/YY)
            Column(modifier = Modifier.weight(1f)) {
                Text("Expiry Date", style = MaterialTheme.typography.caption)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    OutlinedTextField(
                        value = expiryMonth,
                        onValueChange = onExpiryMonthChanged,
                        label = { Text("MM") },
                        modifier = Modifier.weight(1f),
                        isError = expiryError.isNotEmpty(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Right) }
                        ),
                        singleLine = true
                    )
                    
                    OutlinedTextField(
                        value = expiryYear,
                        onValueChange = onExpiryYearChanged,
                        label = { Text("YY") },
                        modifier = Modifier.weight(1f),
                        isError = expiryError.isNotEmpty(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Right) }
                        ),
                        singleLine = true
                    )
                }
                if (expiryError.isNotEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                    ) {
                        Icon(Icons.Default.Error, contentDescription = "Error", modifier = Modifier.padding(end = 4.dp))
                        Text(expiryError, color = MaterialTheme.colors.error)
                    }
                }
            }
            
            // CVV
            Column(modifier = Modifier.weight(1f)) {
                Text("CVV", style = MaterialTheme.typography.caption)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = cvv,
                    onValueChange = onCvvChanged,
                    label = { Text("CVV") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = cvvError.isNotEmpty(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),
                    singleLine = true
                )
                if (cvvError.isNotEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                    ) {
                        Icon(Icons.Default.Error, contentDescription = "Error", modifier = Modifier.padding(end = 4.dp))
                        Text(cvvError, color = MaterialTheme.colors.error)
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isSubmitting
        ) {
            Text("Submit Payment")
        }
    }
} 