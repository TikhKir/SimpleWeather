package com.example.simpleweather.network.openweather.rawmodel

import com.google.gson.annotations.SerializedName


data class RawAlert (
    @SerializedName("sender_name")
    val senderName: String? = null,

    @SerializedName("event")
    val event: String? = null,

    @SerializedName("start")
    val start: Int? = null,

    @SerializedName("end")
    val end: Int? = null,

    @SerializedName("description")
    val description: String? = null
)