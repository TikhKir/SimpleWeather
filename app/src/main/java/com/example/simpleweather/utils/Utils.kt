package com.example.simpleweather.utils

import com.example.simpleweather.R

fun idToIcon(weatherId: Int, isNight: Boolean): Int {
    if (isNight) {
        return when (weatherId) {
            in 200..232 -> return R.drawable.ic_condition_11n
            in 300..321 -> return R.drawable.ic_condition_9n
            in 500..504 -> return R.drawable.ic_condition_10n
            in 511..531 -> return R.drawable.ic_condition_9n
            in 600..622 -> return R.drawable.ic_condition_13n
            in 701..781 -> return R.drawable.ic_condition_50n
            in 803..804 -> return R.drawable.ic_condition_04n
            800 -> return R.drawable.ic_condition_01n
            801 -> return R.drawable.ic_condition_02n
            802 -> return R.drawable.ic_condition_03n
            else -> 0
        }
    } else {
        return when (weatherId) {
            in 200..232 -> return R.drawable.ic_condition_11d
            in 300..321 -> return R.drawable.ic_condition_9d
            in 500..504 -> return R.drawable.ic_condition_10d
            in 511..531 -> return R.drawable.ic_condition_9d
            in 600..622 -> return R.drawable.ic_condition_13d
            in 701..781 -> return R.drawable.ic_condition_50d
            in 803..804 -> return R.drawable.ic_condition_04d
            800 -> return R.drawable.ic_condition_01d
            801 -> return R.drawable.ic_condition_02d
            802 -> return R.drawable.ic_condition_03d
            else -> 0
        }
    }

}