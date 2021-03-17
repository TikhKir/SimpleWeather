package com.example.simpleweather.network.locationiq.rawmodel

import com.google.gson.annotations.SerializedName


data class RawAddress(
    @SerializedName("city")
    val city: String = "",

    @SerializedName("county")
    val county: String = "",

    @SerializedName("state")
    val state: String = "",

    @SerializedName("country")
    val country: String = "",

    @SerializedName("country_code")
    val countryCode: String = ""
)