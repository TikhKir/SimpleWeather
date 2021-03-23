package com.example.simpleweather.utils.favswitcher

import com.example.simpleweather.local.DataApi
import com.example.simpleweather.repository.model.LocationWithCoords
import kotlinx.coroutines.*
import javax.inject.Inject

class DeferredFavouriteSwitcher @Inject constructor(
    private val dataApi: DataApi
)  {

    private val scope = CoroutineScope(Job() + Dispatchers.Default)

    fun addToFavourite(location: LocationWithCoords) {
        scope.launch {
            dataApi.saveNewLocation(location)
            cancel()
        }
    }

    fun deleteFromFavourite(locationId: Long) {
        scope.launch {
            dataApi.deleteLocation(locationId)
            cancel()
        }
    }

}