package com.example.simpleweather.ui.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleweather.domain.model.Location
import com.example.simpleweather.repository.RepositoryApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: RepositoryApi
) : ViewModel() {

    init {
        getSavedLocations()
    }

    val locationLiveData: LiveData<List<Location>> by lazy { getSavedLocations() }

    private fun getSavedLocations(): LiveData<List<Location>> {
        val livedata = MutableLiveData<List<Location>>()
        viewModelScope.launch {
            repository.getSavedLocations()
                .collect { livedata.postValue(it) }
        }
        return livedata
    }


}