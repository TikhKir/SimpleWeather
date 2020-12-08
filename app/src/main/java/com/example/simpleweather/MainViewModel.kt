package com.example.simpleweather

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleweather.repository.RepositoryApi
import com.example.simpleweather.repository.model.DailyWeatherCondition
import com.example.simpleweather.repository.model.LocationWithCoords
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val repository: RepositoryApi,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    //val weatherLiveData = MutableLiveData<List<DailyWeatherCondition>>()

    init {
//        getDailyWeatherCondition(51.681603F, 108.714448F)
//        getHourlyWeatherCondition(51.681603F, 108.714448F)
//        getCurrentWeatherCondition(51.681603F, 108.714448F)
//        getCoordsByCityName("новоильинск")
    }

//    private fun getDailyWeatherCondition(lat: Float, lon: Float) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val dailyList = repository.getDailyCondition(lat, lon)
//                .onEach {
//                    Log.e("DAILY", it.weatherDescription )
//                }
//            delay(10000)
//            //saveDailyForecast(1, dailyList)
//            delay(10000)
//            //deleteLocation(1)
//        }
//    }

//    private fun getHourlyWeatherCondition(lat: Float, lon: Float) {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.getHourlyCondition(lat, lon)
//                .map {
//                    Log.e("HOURLY", it.weatherDescription )
//                }
//        }
//    }

//    private fun getCurrentWeatherCondition(lat: Float, lon: Float) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val current = repository.getCurrentCondition(lat, lon)
//            Log.e("CURRENT", current.weatherDescription )
//        }
//    }

    private fun getCoordsByCityName(cityName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCoordByCityName(cityName)
                .map {
                    Log.e("COORDS", it.fullAddress)

                    saveNewLocation(it)
                }
        }
    }

    private fun saveNewLocation(location: LocationWithCoords) {
        viewModelScope.launch {
            val id = repository.saveNewLocation(location)
            Log.e("LOCATION INSERTED:", id.toString() )
        }
    }

    private fun saveDailyForecast(locationId: Long, listDaily: List<DailyWeatherCondition>) {
        viewModelScope.launch {
            repository.saveDailyForecast(locationId, listDaily)
        }
    }

    private fun deleteLocation(locationId: Long) {
        viewModelScope.launch {
            val result = repository.deleteLocation(locationId)
            Log.e("LOCATION DEL", result.toString() )
        }
    }

}