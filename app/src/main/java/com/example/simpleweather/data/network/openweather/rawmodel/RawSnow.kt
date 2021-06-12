package com.example.simpleweather.data.network.openweather.rawmodel

import com.google.gson.annotations.SerializedName

data class RawSnow(
    @SerializedName("1h")
    val last_hour: Float? = null,
)