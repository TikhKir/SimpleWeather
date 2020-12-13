package com.example.simpleweather.ui.conditiondetails

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleweather.repository.RepositoryApi
import com.example.simpleweather.repository.model.CurrentWeatherCondition
import com.example.simpleweather.repository.model.DailyWeatherCondition
import com.example.simpleweather.repository.model.HourlyWeatherCondition
import com.example.simpleweather.utils.datawrappers.ResultType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ConditionDetailsViewModel @ViewModelInject constructor(
    private val repository: RepositoryApi
) : ViewModel() {

    val currentLiveData = MutableLiveData<CurrentWeatherCondition>()
    val hourlyListLiveData = MutableLiveData<List<HourlyWeatherCondition>>()
    val dailyListLiveData = MutableLiveData<List<DailyWeatherCondition>>()


    init {
        //initList()
        //getHourlyWeatherCondition(51.681603F, 108.714448F)
//        getHourlyWeatherCondition(1)
//        getCurrentWeatherCondition(1)
//        getDailyWeatherCondition(1)
    }


    fun getHourlyWeatherCondition(locationId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getHourlyCondition(locationId)
                .collect { response ->
                    if (response.resultType == ResultType.SUCCESS) {
                        hourlyListLiveData.postValue(response.data)
                    } else {
                        Log.e("HOURLY_RESPONSE", response.error?.message.toString())
                    }
                }
        }
    }



    fun getCurrentWeatherCondition(locationId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCurrentCondition(locationId)
                .collect { response ->
                    if (response.resultType == ResultType.SUCCESS) {
                        currentLiveData.postValue(response.data)
                    } else {
                        Log.e("CURRENT_RESPONSE", response.error?.message.toString())
                    }
                }
        }
    }



    fun getDailyWeatherCondition(locationId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getDailyCondition(locationId)
                .collect { response ->
                    if (response.resultType == ResultType.SUCCESS) {
                        dailyListLiveData.postValue(response.data)
                    } else {
                        Log.e("DAILY_RESPONSE", response.error?.message.toString())
                    }
                }
        }
    }

    fun getHourlyWeatherCondition(lat: Float, lon: Float) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getHourlyCondition(lat, lon)
                .collect { response ->
                    if (response.resultType == ResultType.SUCCESS) {
                        hourlyListLiveData.postValue(response.data)
                    } else {
                        Log.e("HOURLY_RESPONSE", response.error?.message.toString())
                    }
                }
        }
    }

    fun getCurrentWeatherCondition(lat: Float, lon: Float) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCurrentCondition(lat, lon)
                .collect { response ->
                    if (response.resultType == ResultType.SUCCESS) {
                        currentLiveData.postValue(response.data)
                    } else {
                        Log.e("CURRENT_RESPONSE", response.error?.message.toString())
                    }
                }
        }
    }

    fun getDailyWeatherCondition(lat: Float, lon: Float) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getDailyCondition(lat, lon)
                .collect { response ->
                    if (response.resultType == ResultType.SUCCESS) {
                        dailyListLiveData.postValue(response.data)
                    } else {
                        Log.e("DAILY_RESPONSE", response.error?.message.toString())
                    }
                }
        }
    }
}