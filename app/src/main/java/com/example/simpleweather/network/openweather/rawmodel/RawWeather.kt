package com.example.simpleweather.network.openweather.rawmodel

import com.google.gson.annotations.SerializedName


data class RawWeather(
    @SerializedName("id")
    val id: Int,

    @SerializedName("main")
    val main: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("icon")
    val icon: String
)