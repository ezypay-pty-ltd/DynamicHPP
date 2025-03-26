package com.ezypay.dhpp.model

import java.util.Calendar

actual fun getCurrentYear(): Int {
    return Calendar.getInstance().get(Calendar.YEAR) % 100 // Last two digits of year
}

actual fun getCurrentMonth(): Int {
    return Calendar.getInstance().get(Calendar.MONTH) + 1 // Calendar months are 0-based
} 