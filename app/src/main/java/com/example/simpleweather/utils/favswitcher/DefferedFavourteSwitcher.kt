package com.example.simpleweather.utils.favswitcher

import com.example.simpleweather.repository.model.LocationWithCoords

interface DeferredFavouriteSwitcher {

    fun addToFavourite(location: LocationWithCoords)

    fun deleteFromFavourite(locationId: Long)
}