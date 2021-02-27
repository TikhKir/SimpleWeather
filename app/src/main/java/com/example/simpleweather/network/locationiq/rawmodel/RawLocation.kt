package com.example.simpleweather.network.locationiq.rawmodel

import com.example.simpleweather.repository.model.LocationWithCoords
import com.example.simpleweather.utils.Constants
import com.google.gson.annotations.SerializedName
import org.threeten.bp.Instant


data class RawLocation(
    @SerializedName("place_id")
    val locationId: String? = null,

    @SerializedName("licence")
    val licence: String? = null,

    @SerializedName("osm_type")
    val osmType: String? = null,

    @SerializedName("osm_id")
    val osmId: String? = null,

    @SerializedName("boundingbox")
    val boundingBox: List<String> = ArrayList(),

    @SerializedName("lon")
    val lon: Float,

    @SerializedName("lat")
    val lat: Float,

    @SerializedName("display_name")
    val displayName: String,

    @SerializedName("class")
    val _class: String? = null,

    @SerializedName("type")
    val type: String? = null,

    @SerializedName("importance")
    val importance: Float? = null,

    @SerializedName("icon")
    val icon: String? = null,

    @SerializedName("address")
    val rawAddress: RawAddress = RawAddress()
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