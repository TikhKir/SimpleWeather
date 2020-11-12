package com.example.simpleweather.local.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.simpleweather.repository.model.LocationWithCoords

@Entity(
    tableName = "locations",
    indices = [Index(value = ["fullAddress"], unique = true)]
)
data class LocationDB(
    val longitude: Float,
    val latitude: Float,
    val fullAddress: String,
    val city: String,
    val county: String?,
    val state: String?,
    val country: String?
) {
    @PrimaryKey(autoGenerate = true)
    var locationId: Int? = null

    fun toLocationWithCoords(): LocationWithCoords {
        return LocationWithCoords(
            locationId!!,
            fullAddress,
            longitude,
            latitude,
            city,
            county,
            state,
            country
            )
    }
}