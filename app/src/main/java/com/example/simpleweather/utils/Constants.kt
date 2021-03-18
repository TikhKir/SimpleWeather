package com.example.simpleweather.utils

object Constants {
    //location service
    const val REQUEST_CODE_LOCATION_PERMISSIONS = 0

    //work manager
    const val REFRESH_CONDITIONS_WORK = "REFRESH_CONDITIONS_WORK"

    const val REFRESH_INTERVAL = 21600  //Duration.ofHours(6).seconds
    const val REFRESH_INTERVAL_CURRENT = 1200 //Duration.ofMinutes(20).seconds

}

