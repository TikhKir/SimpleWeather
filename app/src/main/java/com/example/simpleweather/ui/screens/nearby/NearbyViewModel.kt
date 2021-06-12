package com.example.simpleweather.ui.screens.nearby

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleweather.domain.model.Location
import com.example.simpleweather.repository.RepositoryApi
import com.example.simpleweather.utils.State
import com.example.simpleweather.utils.datawrappers.ResultType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NearbyViewModel @Inject constructor(
    private val repository: RepositoryApi
) : ViewModel() {

    val locations: LiveData<List<Location>> get() = locationsLiveData
    val state: LiveData<State> get() = stateLiveData

    private val locationsLiveData = MutableLiveData<List<Location>>()
    private val stateLiveData = MutableLiveData<State>(State.Default())

    fun loadLocationsByCoords(lat: Float, lon: Float) = viewModelScope.launch {
        stateLiveData.postValue(State.Loading())
        val locations = repository.getCityNameByCoords(lat, lon)
        if (locations.resultType == ResultType.SUCCESS) {
            locationsLiveData.postValue(locations.data!!)
            stateLiveData.postValue(State.Success())
        } else {
            Log.e("COORDS to LOCATIONS", locations.error?.message.toString())
            stateLiveData.postValue(State.Error(locations.error?.message.toString()))
        }

    }
}
