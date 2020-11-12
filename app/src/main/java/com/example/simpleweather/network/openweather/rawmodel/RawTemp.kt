package com.example.simpleweather.network.openweather.rawmodel
import com.google.gson.annotations.SerializedName


data class RawTemp (
    @SerializedName("day")
    val day: Float? = null,

    @SerializedName("min")
    val min: Float? = null,

    @SerializedName("max")
    val max: Float? = null,

    @SerializedName("night")
    val night: Float? = null,

    @SerializedName("eve")
    val eve: Float? = null,

    @SerializedName("morn")
    val morn: Float? = null
)