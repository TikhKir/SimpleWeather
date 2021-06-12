package com.example.simpleweather.data.local.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.simpleweather.domain.model.Location

@Entity(
    tableName = "locations",
    indices = [Index(value = ["fullAddress"], unique = true)]
)
data class LocationDB(
    val longitude: Float,
    val latitude: Float,
    val fullAddress: String,
    val city: String,
    val county: String,
    val state: String,
    val country: String,
    val refreshTimeDaily: Long,
    val refreshTimeHourly: Long,
    val refreshTimeCurrent: Long
) {
    @PrimaryKey(autoGenerate = true)
    var locationId: Long? = null

    fun toLocation(): Location {
        return Location(
            locationId!!,
            fullAddress,
            longitude,
            latitude,
            city,
            county,
            state,
            country,
            refreshTimeDaily,
            refreshTimeHourly,
            refreshTimeCurrent
            )
    }
}