package com.example.simpleweather.sharedpreferences

import kotlinx.coroutines.flow.StateFlow

interface SharedPreferencesSource {

    fun getSharedPreferences(): StateFlow<MutableMap<String, *>>
}