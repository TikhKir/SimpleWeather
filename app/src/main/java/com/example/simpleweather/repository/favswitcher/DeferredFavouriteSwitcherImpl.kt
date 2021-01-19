package com.example.simpleweather.repository.favswitcher

import com.example.simpleweather.local.DataApi
import com.example.simpleweather.repository.model.LocationWithCoords
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class DeferredFavouriteSwitcherImpl @Inject constructor(
    private val dataApi: DataApi
) : DeferredFavouriteSwitcher {

    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    override fun addToFavourite(location: LocationWithCoords) {
        scope.launch {
        dataApi.saveNewLocation(location)
        }
    }

    override fun deleteFromFavourite(locationId: Long) {
        scope.launch {
            dataApi.deleteLocation(locationId)
        }
    }

}