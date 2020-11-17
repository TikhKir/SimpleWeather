package com.example.simpleweather.ui.conditiondetails

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.simpleweather.repository.RepositoryApi
import com.example.simpleweather.repository.model.HourlyWeatherCondition
import kotlin.random.Random
import kotlin.random.nextInt

class ConditionDetailsViewModel @ViewModelInject constructor(
    private val repository: RepositoryApi
) : ViewModel() {

    val fakeListLiveData = MutableLiveData<List<HourlyWeatherCondition>>()
    private val fakeHourlyConditionList = mutableListOf<HourlyWeatherCondition>()

    init {
        initList()
    }

    fun initList() {
        fakeHourlyConditionList.clear()
        for (i in 1..7) {
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
}