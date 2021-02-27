package com.example.simpleweather.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleweather.repository.RepositoryApi
import com.example.simpleweather.repository.model.LocationWithCoords
import com.example.simpleweather.utils.datawrappers.ResultType
import com.example.simpleweather.utils.datawrappers.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: RepositoryApi
) : ViewModel() {

    private val locations = MutableLiveData<List<LocationWithCoords>>()
    private val state = MutableLiveData<State>(State.Default())

    val locationsLiveData: LiveData<List<LocationWithCoords>> get() = locations
    val stateLiveData: LiveData<State> get() = state


    fun searchLocations(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            state.postValue(State.Loading())
            val locationList = repository.getCoordByCityName(query)
            if (locationList.resultType == ResultType.SUCCESS) {
                locations.postValue(locationList.data!!)
                state.postValue(State.Success())
            } else {
                Log.e("SEARCH LOCATION", locationList.error?.message.toString())
                state.postValue(State.Error(locationList.error?.message.toString()))
            }
        }
    }
}