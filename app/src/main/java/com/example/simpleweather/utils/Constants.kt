package com.example.simpleweather.utils

object Constants {

    const val REQUEST_CODE_LOCATION_PERMISSIONS = 0

    const val REFRESH_CONDITIONS_WORK = "REFRESH_CONDITIONS_WORK"

    const val REFRESH_INTERVAL = 21600  //Duration.ofHours(6).seconds
    const val REFRESH_INTERVAL_CURRENT = 1200 //Duration.ofMinutes(20).seconds

    //shared preferences keys
    const val SHARED_PREF_KEY_UNITS_DEGREE = "units_degree"
    const val SHARED_PREF_KEY_UNITS_WIND_SPEED = "units_wind_speed"
    const val SHARED_PREF_KEY_UNITS_PRESSURE = "units_pressure"
    const val SHARED_PREF_KEY_UPDATE_INTERVAL = "update_interval"

    const val UNITS_CELSIUS = "units_celsius"
    const val UNITS_FAHRENHEIT = "units_fahrenheit"
}