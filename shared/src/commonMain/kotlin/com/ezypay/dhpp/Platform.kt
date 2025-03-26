package com.ezypay.dhpp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform