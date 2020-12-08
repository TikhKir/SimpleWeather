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
import kotlinx.coroutines.CoroutineName
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
        getHourlyWeatherCondition(1)
        getCurrentWeatherCondition(1)
        getDailyWeatherCondition(1)
    }

//    fun initList() {
//        hourlyConditionList.clear()
//        val rand = Random.nextInt(48)
//        for (i in 1..rand) {
//            val temp = HourlyWeatherCondition(
//                i,
//                0,
//                Random.nextInt(-10..10).toFloat(),
//                0F,
//                0,
//                0,
//                Random.nextInt(0..30).toFloat(),
//                Random.nextInt(0..360),
//                411,
//                "Bad weather",
//                "Very bad weather with thunder",
//                "14",
//                Random.nextInt(100).toFloat(),
//                Random.nextInt(10).toFloat(),
//                Random.nextInt(10).toFloat()
//            )
//            hourlyConditionList.add(temp)
//        }
//        hourlyListLiveData.postValue(hourlyConditionList)
//    }

//    fun getHourlyWeatherCondition(lat: Float, lon: Float) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val response = repository.getHourlyCondition(lat, lon)
//            if (response.resultType == ResultType.SUCCESS) {
//                hourlyConditionList = response.data?.toMutableList()!!
//                hourlyListLiveData.postValue(hourlyConditionList)
//            } else {
//                Log.e("HOURLY_RESPONSE", response.error?.message.toString())
//            }
//
//        }
//    }

    fun getHourlyWeatherCondition(locationId: Long) {
        viewModelScope.launch(Dispatchers.IO + CoroutineName("GET_HOURLY_COR")) {
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
}