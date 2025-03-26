package com.ezypay.dhpp.model

data class CreditCard(
    val number: String = "",
    val holderName: String = "",
    val expiryMonth: String = "",
    val expiryYear: String = "",
    val cvv: String = ""
) {
    companion object {
        private val CARD_NUMBER_PATTERN = Regex("^[0-9]{16}$")
        private val CVV_PATTERN = Regex("^[0-9]{3,4}$")
        
        fun validateNumber(number: String): Boolean {
            val digitsOnly = number.replace("\\D".toRegex(), "")
            return CARD_NUMBER_PATTERN.matches(digitsOnly) && validateLuhn(digitsOnly)
        }
        
        fun validateName(name: String): Boolean {
            return name.trim().length >= 3
        }
        
        fun validateExpiry(month: String, year: String): Boolean {
            val monthInt = month.toIntOrNull() ?: return false
            val yearInt = year.toIntOrNull() ?: return false
            
            if (monthInt < 1 || monthInt > 12) return false
            
            val currentYear = getCurrentYear()
            val currentMonth = getCurrentMonth()
            
            return if (yearInt < currentYear) {
                false
            } else if (yearInt == currentYear) {
                monthInt >= currentMonth
            } else {
                true
            }
        }
        
        fun validateCvv(cvv: String): Boolean {
            return CVV_PATTERN.matches(cvv)
        }
        
        private fun validateLuhn(cardNumber: String): Boolean {
            var sum = 0
            var alternate = false
            
            for (i in cardNumber.length - 1 downTo 0) {
                var digit = cardNumber[i].toString().toInt()
                
                if (alternate) {
                    digit *= 2
                    if (digit > 9) {
                        digit -= 9
                    }
                }
                
                sum += digit
                alternate = !alternate
            }
            
            return sum % 10 == 0
        }
    }
}

// Platform-specific date functions
expect fun getCurrentYear(): Int
expect fun getCurrentMonth(): Int 