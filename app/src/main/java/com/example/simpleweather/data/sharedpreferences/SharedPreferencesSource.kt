package com.example.simpleweather.data.sharedpreferences

import kotlinx.coroutines.flow.StateFlow

interface SharedPreferencesSource {

    fun getSharedPreferences(): StateFlow<MutableMap<String, *>>
}