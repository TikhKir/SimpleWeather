package com.example.simpleweather.ui.nearby

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleweather.repository.RepositoryApi
import com.example.simpleweather.repository.model.LocationWithCoords
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NearbyViewModel @ViewModelInject constructor(
    private val repository: RepositoryApi
) : ViewModel() {

    val locationsLiveData = MutableLiveData<List<LocationWithCoords>>()

    fun loadLocationsByCoords(lat: Float, lon: Float) {
        viewModelScope.launch(Dispatchers.IO) {
            val locations = repository.getCityNameByCoords(lat, lon)
            locationsLiveData.postValue(locations)
        }
    }
}