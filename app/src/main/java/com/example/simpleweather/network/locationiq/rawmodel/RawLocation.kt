package com.example.simpleweather.network.locationiq.rawmodel

import com.example.simpleweather.repository.model.LocationWithCoords
import com.example.simpleweather.utils.Constants
import com.google.gson.annotations.SerializedName
import org.threeten.bp.Instant


data class RawLocation(
    @SerializedName("place_id")
    val locationId: String = "",

    @SerializedName("licence")
    val licence: String = "",

    @SerializedName("osm_type")
    val osmType: String = "",

    @SerializedName("osm_id")
    val osmId: String = "",

    @SerializedName("boundingbox")
    val boundingBox: List<String>,

    @SerializedName("lon")
    val lon: Float = 0F,

    @SerializedName("lat")
    val lat: Float = 0F,

    @SerializedName("display_name")
    val displayName: String = "",

    @SerializedName("class")
    val _class: String = "",

    @SerializedName("type")
    val type: String = "",

    @SerializedName("importance")
    val importance: Float = 0F,

    @SerializedName("icon")
    val icon: String = "",

    @SerializedName("address")
    val rawAddress: RawAddress
)
{

    fun toLocationWithCoords(): LocationWithCoords {
        val initialTime = Instant.now().minusSeconds(Constants.REFRESH_INTERVAL.toLong()).epochSecond
        return LocationWithCoords(
            -1,
            displayName,
            lon,
            lat,
            rawAddress.city,
            rawAddress.county,
            rawAddress.state,
            rawAddress.country,
            initialTime,
            initialTime,
            initialTime
        )
    }

}