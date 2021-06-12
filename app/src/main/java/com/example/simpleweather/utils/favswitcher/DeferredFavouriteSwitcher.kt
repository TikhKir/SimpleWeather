package com.example.simpleweather.utils.favswitcher

import com.example.simpleweather.data.local.DataApi
import com.example.simpleweather.domain.model.Location
import kotlinx.coroutines.*
import javax.inject.Inject

class DeferredFavouriteSwitcher @Inject constructor(
    private val dataApi: DataApi
)  {

    private val scope = CoroutineScope(Job() + Dispatchers.Default)

    fun addToFavourite(location: Location) {
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