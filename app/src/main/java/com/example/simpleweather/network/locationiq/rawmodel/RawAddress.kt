package com.example.simpleweather.network.locationiq.rawmodel

import com.google.gson.annotations.SerializedName


data class RawAddress(
    @SerializedName("city")
    val city: String = "",

    @SerializedName("county")
    val county: String? = null,

    @SerializedName("state")
    val state: String? = null,

    @SerializedName("country")
    val country: String? = null,

    @SerializedName("country_code")
    val countryCode: String? = null
)