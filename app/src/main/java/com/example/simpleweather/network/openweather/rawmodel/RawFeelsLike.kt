package com.example.simpleweather.network.openweather.rawmodel

import com.google.gson.annotations.SerializedName


data class RawFeelsLike (
    @SerializedName("day")
    val day: Float,

    @SerializedName("night")
    val night: Float,

    @SerializedName("eve")
    val eve: Float,

    @SerializedName("morn")
    val morn: Float
)