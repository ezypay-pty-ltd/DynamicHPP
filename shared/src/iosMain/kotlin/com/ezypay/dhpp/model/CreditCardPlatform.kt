package com.ezypay.dhpp.model

import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate

actual fun getCurrentYear(): Int {
    val calendar = NSCalendar.currentCalendar()
    val components = calendar.components(NSCalendarUnitYear, NSDate())
    return components.year.toInt() % 100 // Last two digits of year
}

actual fun getCurrentMonth(): Int {
    val calendar = NSCalendar.currentCalendar()
    val components = calendar.components(NSCalendarUnitMonth, NSDate())
    return components.month.toInt()
} 