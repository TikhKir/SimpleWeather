package com.example.simpleweather.ui.home

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleweather.repository.RepositoryApi
import com.example.simpleweather.repository.model.LocationWithCoords
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel @ViewModelInject constructor(
    private val repository: RepositoryApi
) : ViewModel() {

    init {
        getSavedLocations()
    }

    val locationLiveData: LiveData<List<LocationWithCoords>> by lazy { getSavedLocations() }

    private fun getSavedLocations(): LiveData<List<LocationWithCoords>> {
        val livedata = MutableLiveData<List<LocationWithCoords>>()
        viewModelScope.launch(Dispatchers.IO) {
            val locations = repository.getSavedLocations()
                .collect {
                    livedata.postValue(it)
                    it.map { Log.e("LOCATION", it.fullAddress) }
                }
        }
        return livedata
    }


}