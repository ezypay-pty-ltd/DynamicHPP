package com.ezypay.dhpp

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ezypay.dhpp.ui.CreditCardFormScreen
import com.ezypay.dhpp.viewmodel.CreditCardViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val viewModel: CreditCardViewModel = viewModel()
        CreditCardFormScreen(viewModel)
    }
}