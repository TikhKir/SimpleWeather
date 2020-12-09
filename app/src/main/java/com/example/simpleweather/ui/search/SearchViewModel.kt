package com.example.simpleweather.ui.search

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

class SearchViewModel @ViewModelInject constructor(
    private val repository: RepositoryApi
) : ViewModel() {

    val locationsLiveData = MutableLiveData<List<LocationWithCoords>>()


    fun searchLocations(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val locationList = repository.getCoordByCityName(query)
            if (locationList.resultType == ResultType.SUCCESS) {
                locationsLiveData.postValue(locationList.data)
            } else {
                Log.e("SEARCH LOCATION", locationList.error?.message.toString())
            }

        }
    }
}