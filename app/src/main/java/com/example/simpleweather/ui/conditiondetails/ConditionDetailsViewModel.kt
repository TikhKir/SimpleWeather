package com.example.simpleweather.ui.conditiondetails

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleweather.repository.RepositoryApi
import com.example.simpleweather.repository.model.HourlyWeatherCondition
import com.example.simpleweather.utils.datawrappers.ResultType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.random.nextInt

class ConditionDetailsViewModel @ViewModelInject constructor(
    private val repository: RepositoryApi
) : ViewModel() {

    val fakeListLiveData = MutableLiveData<List<HourlyWeatherCondition>>()
    private var fakeHourlyConditionList = mutableListOf<HourlyWeatherCondition>()

    init {
        //initList()
        //getHourlyWeatherCondition(51.681603F, 108.714448F)
        getHourlyWeatherCondition(1)
    }

    fun initList() {
        fakeHourlyConditionList.clear()
        val rand = Random.nextInt(48)
        for (i in 1..rand) {
            val temp = HourlyWeatherCondition(
                i,
                Random.nextInt(-10..10).toFloat(),
                0F,
                0,
                0,
                Random.nextInt(0..30).toFloat(),
                Random.nextInt(0..360),
                411,
                "Bad weather",
                "Very bad weather with thunder",
                "14",
                Random.nextInt(100).toFloat(),
                Random.nextInt(10).toFloat(),
                Random.nextInt(10).toFloat()
            )
            fakeHourlyConditionList.add(temp)
        }
        fakeListLiveData.postValue(fakeHourlyConditionList)
    }

    fun getHourlyWeatherCondition(lat: Float, lon: Float) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getHourlyCondition(lat, lon)
            if (response.resultType == ResultType.SUCCESS) {
                fakeHourlyConditionList = response.data?.toMutableList()!!
                fakeListLiveData.postValue(fakeHourlyConditionList)
            } else {
                Log.e("HOURLY_RESPONSE", response.error?.message.toString())
            }

        }
    }

    fun getHourlyWeatherCondition(location: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getHourlyCondition(location)
                .collect { response ->
                    if (response.resultType == ResultType.SUCCESS) {
                        fakeHourlyConditionList = response.data?.toMutableList()!!
                        fakeListLiveData.postValue(fakeHourlyConditionList)
                    } else {
                        Log.e("HOURLY_RESPONSE", response.error?.message.toString())
                    }
                }


        }
    }
}