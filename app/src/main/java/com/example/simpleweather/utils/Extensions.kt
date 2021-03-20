package com.example.simpleweather.utils

import java.text.DecimalFormat

fun Float.toUIFormat(): String {
    val decimalFormat = DecimalFormat("#.#")
    return decimalFormat.format(this)
}