package com.example.simpleweather.network.openweather.rawmodel

import com.google.gson.annotations.SerializedName


data class RawAlert (
    @SerializedName("sender_name")
    val senderName: String,

    @SerializedName("event")
    val event: String,

    @SerializedName("start")
    val start: Int,

    @SerializedName("end")
    val end: Int,

    @SerializedName("description")
    val description: String
)