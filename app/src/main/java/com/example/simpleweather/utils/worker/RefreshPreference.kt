package com.example.simpleweather.utils.worker

const val BACKGROUND_REFRESH_WORK = "BACKGROUND_REFRESH_WORK"

const val INTERVAL_PREFERENCE_KEY = "update_interval"

enum class RefreshPrefValues(val key: String, val value: Long) {
    INTERVAL_24_HOURS("update_24_hours", 24),
    INTERVAL_12_HOURS("update_12_hours", 12),
    INTERVAL_6_HOURS("update_6_hours", 6),
    INTERVAL_3_HOURS("update_3_hours", 3)
}