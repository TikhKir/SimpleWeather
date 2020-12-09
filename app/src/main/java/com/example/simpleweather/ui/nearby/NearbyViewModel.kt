package com.example.simpleweather.ui.nearby

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleweather.repository.RepositoryApi
import com.example.simpleweather.repository.model.LocationWithCoords
import com.example.simpleweather.utils.datawrappers.ResultType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NearbyViewModel @ViewModelInject constructor(
    private val repository: RepositoryApi
) : ViewModel() {

    val locationsLiveData = MutableLiveData<List<LocationWithCoords>>()

    fun loadLocationsByCoords(lat: Float, lon: Float) {
        viewModelScope.launch(Dispatchers.IO) {
            val locations = repository.getCityNameByCoords(lat, lon)
            if (locations.resultType == ResultType.SUCCESS) {
                locationsLiveData.postValue(locations.data)
            } else {
                Log.e("COORDS to LOCATIONS", locations.error?.message.toString() )
            }

        }
    }
}