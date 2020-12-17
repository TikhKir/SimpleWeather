package com.example.simpleweather

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleweather.repository.RepositoryApi
import com.example.simpleweather.repository.model.LocationWithCoords
import com.example.simpleweather.utils.datawrappers.ResultType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val repository: RepositoryApi,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {


    init {
//          getCoordsByCityName("новоильинск")
    }


    private fun getCoordsByCityName(cityName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getCoordByCityName(cityName)
                if (response.resultType == ResultType.SUCCESS) {
                    response.data?.map {
                        saveNewLocation(it)
                    }
                } else {
                    Log.e("GEOCODING", response.error?.message.toString() )
                }
        }
    }

    private fun saveNewLocation(location: LocationWithCoords) {
        viewModelScope.launch {
            val id = repository.saveNewLocation(location)
            Log.e("LOCATION INSERTED:", id.toString() )
        }
    }

    private fun deleteLocation(locationId: Long) {
        viewModelScope.launch {
            val result = repository.deleteLocation(locationId)
            Log.e("LOCATION DEL", result.toString() )
        }
    }

}