package com.ezypay.dhpp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezypay.dhpp.model.CreditCard
import com.ezypay.dhpp.model.getCurrentMonth
import com.ezypay.dhpp.model.getCurrentYear
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreditCardViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CreditCardUiState())
    val uiState: StateFlow<CreditCardUiState> = _uiState.asStateFlow()

    fun updateCardNumber(number: String) {
        _uiState.update { currentState ->
            val formattedNumber = formatCardNumber(number)
            val isValid = CreditCard.validateNumber(number)
            currentState.copy(
                creditCard = currentState.creditCard.copy(number = formattedNumber),
                numberError = if (formattedNumber.isNotEmpty() && !isValid) "Invalid card number" else ""
            )
        }
    }

    fun updateCardholderName(name: String) {
        _uiState.update { currentState ->
            val isValid = CreditCard.validateName(name)
            currentState.copy(
                creditCard = currentState.creditCard.copy(holderName = name),
                nameError = if (name.isNotEmpty() && !isValid) "Name must be at least 3 characters" else ""
            )
        }
    }

    fun updateExpiryMonth(month: String) {
        if (month.length <= 2 && month.all { it.isDigit() }) {
            _uiState.update { currentState ->
                val isValid = validateExpiryDate(month, currentState.creditCard.expiryYear)
                currentState.copy(
                    creditCard = currentState.creditCard.copy(expiryMonth = month),
                    expiryError = if ((month.isNotEmpty() || currentState.creditCard.expiryYear.isNotEmpty()) && !isValid) 
                        "Invalid expiry date" else ""
                )
            }
        }
    }

    fun updateExpiryYear(year: String) {
        if (year.length <= 2 && year.all { it.isDigit() }) {
            _uiState.update { currentState ->
                val isValid = validateExpiryDate(currentState.creditCard.expiryMonth, year)
                currentState.copy(
                    creditCard = currentState.creditCard.copy(expiryYear = year),
                    expiryError = if ((year.isNotEmpty() || currentState.creditCard.expiryMonth.isNotEmpty()) && !isValid) 
                        "Invalid expiry date" else ""
                )
            }
        }
    }

    fun updateCvv(cvv: String) {
        if (cvv.length <= 4 && cvv.all { it.isDigit() }) {
            _uiState.update { currentState ->
                val isValid = CreditCard.validateCvv(cvv)
                currentState.copy(
                    creditCard = currentState.creditCard.copy(cvv = cvv),
                    cvvError = if (cvv.isNotEmpty() && !isValid) "Invalid CVV" else ""
                )
            }
        }
    }

    fun processPayment() {
        val card = _uiState.value.creditCard
        val isNumberValid = CreditCard.validateNumber(card.number)
        val isNameValid = CreditCard.validateName(card.holderName)
        val isExpiryValid = CreditCard.validateExpiry(card.expiryMonth, card.expiryYear)
        val isCvvValid = CreditCard.validateCvv(card.cvv)

        if (isNumberValid && isNameValid && isExpiryValid && isCvvValid) {
            _uiState.update { it.copy(isSubmitting = true) }
            
            // Simulate API call
            viewModelScope.launch {
                try {
                    // In a real app, this would be an API call
                    kotlinx.coroutines.delay(1500)
                    _uiState.update { it.copy(isSubmitting = false, isSubmissionSuccessful = true) }
                } catch (e: Exception) {
                    _uiState.update { 
                        it.copy(
                            isSubmitting = false, 
                            generalError = "Payment processing failed: ${e.message}"
                        )
                    }
                }
            }
        } else {
            // Update all validation errors
            _uiState.update { currentState ->
                currentState.copy(
                    numberError = if (!isNumberValid) "Invalid card number" else "",
                    nameError = if (!isNameValid) "Invalid name" else "",
                    expiryError = if (!isExpiryValid) "Invalid expiry date" else "",
                    cvvError = if (!isCvvValid) "Invalid CVV" else "",
                    hasValidationErrors = true
                )
            }
        }
    }

    fun resetSubmissionState() {
        _uiState.update { it.copy(isSubmissionSuccessful = false, generalError = "") }
    }

    private fun formatCardNumber(number: String): String {
        val digitsOnly = number.replace("\\D".toRegex(), "")
        val formatted = StringBuilder()
        
        for (i in digitsOnly.indices) {
            if (i > 0 && i % 4 == 0) {
                formatted.append(" ")
            }
            formatted.append(digitsOnly[i])
        }
        
        return formatted.toString()
    }

    private fun validateExpiryDate(month: String, year: String): Boolean {
        if (month.isEmpty() || year.isEmpty()) return true
        return CreditCard.validateExpiry(month, year)
    }
}

data class CreditCardUiState(
    val creditCard: CreditCard = CreditCard(),
    val numberError: String = "",
    val nameError: String = "",
    val expiryError: String = "",
    val cvvError: String = "",
    val generalError: String = "",
    val isSubmitting: Boolean = false,
    val isSubmissionSuccessful: Boolean = false,
    val hasValidationErrors: Boolean = false
) 