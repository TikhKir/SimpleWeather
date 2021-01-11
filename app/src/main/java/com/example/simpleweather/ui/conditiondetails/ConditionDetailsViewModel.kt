package com.example.simpleweather.ui.conditiondetails

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.simpleweather.repository.RepositoryApi
import com.example.simpleweather.repository.model.CurrentWeatherCondition
import com.example.simpleweather.repository.model.DailyWeatherCondition
import com.example.simpleweather.repository.model.HourlyWeatherCondition
import com.example.simpleweather.repository.model.LocationWithCoords
import com.example.simpleweather.utils.datawrappers.ResultType
import com.example.simpleweather.utils.datawrappers.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ConditionDetailsViewModel @ViewModelInject constructor(
    private val repository: RepositoryApi
) : ViewModel() {

    private val currentCondition = MutableLiveData<CurrentWeatherCondition>()
    private val hourlyCondition = MutableLiveData<List<HourlyWeatherCondition>>()
    private val dailyCondition = MutableLiveData<List<DailyWeatherCondition>>()
    private val stateCurrent: MutableLiveData<State> = MutableLiveData(State.Default())
    private val stateHourly: MutableLiveData<State> = MutableLiveData(State.Default())
    private val stateDaily: MutableLiveData<State> = MutableLiveData(State.Default())
    private val unionStates = MediatorLiveData<State>()

    val currentLiveData: LiveData<CurrentWeatherCondition> get() = currentCondition
    val hourlyLiveData: LiveData<List<HourlyWeatherCondition>> get() = hourlyCondition
    val dailyLivaData: LiveData<List<DailyWeatherCondition>> get() = dailyCondition
    val stateLiveData: LiveData<State> get() = unionStates


    init {
        mergeStates()
    }


    fun getHourlyWeatherCondition(locationId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            stateHourly.postValue(State.Loading())
            repository.getHourlyCondition(locationId)
                .collect { response ->
                    if (response.resultType == ResultType.SUCCESS) {
                        hourlyCondition.postValue(response.data)
                        stateHourly.postValue(State.Success())
                    } else {
                        Log.e("HOURLY_RESPONSE", response.error?.message.toString())
                        stateHourly.postValue(State.Error())
                    }
                }
        }
    }

    fun getCurrentWeatherCondition(locationId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            stateCurrent.postValue(State.Loading())
            repository.getCurrentCondition(locationId)
                .collect { response ->
                    if (response.resultType == ResultType.SUCCESS) {
                        currentCondition.postValue(response.data)
                        stateCurrent.postValue(State.Success())
                    } else {
                        Log.e("CURRENT_RESPONSE", response.error?.message.toString())
                        stateCurrent.postValue(State.Error())
                    }
                }
        }
    }

    fun getDailyWeatherCondition(locationId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            stateDaily.postValue(State.Loading())
            repository.getDailyCondition(locationId)
                .collect { response ->
                    if (response.resultType == ResultType.SUCCESS) {
                        dailyCondition.postValue(response.data)
                        stateDaily.postValue(State.Success())
                    } else {
                        Log.e("DAILY_RESPONSE", response.error?.message.toString())
                        stateDaily.postValue(State.Error())
                    }
                }
        }
    }


    fun getHourlyWeatherCondition(lat: Float, lon: Float) {
        viewModelScope.launch(Dispatchers.IO) {
            stateHourly.postValue(State.Loading())
            repository.getHourlyCondition(lat, lon)
                .collect { response ->
                    if (response.resultType == ResultType.SUCCESS) {
                        hourlyCondition.postValue(response.data)
                        stateHourly.postValue(State.Success())
                    } else {
                        Log.e("HOURLY_RESPONSE", response.error?.message.toString())
                        stateHourly.postValue(State.Error())
                    }
                }
        }
    }

    fun getCurrentWeatherCondition(lat: Float, lon: Float) {
        viewModelScope.launch(Dispatchers.IO) {
            stateCurrent.postValue(State.Loading())
            repository.getCurrentCondition(lat, lon)
                .collect { response ->
                    if (response.resultType == ResultType.SUCCESS) {
                        currentCondition.postValue(response.data)
                        stateCurrent.postValue(State.Success())
                    } else {
                        Log.e("CURRENT_RESPONSE", response.error?.message.toString())
                        stateCurrent.postValue(State.Error())
                    }
                }
        }
    }

    fun getDailyWeatherCondition(lat: Float, lon: Float) {
        viewModelScope.launch(Dispatchers.IO) {
            stateDaily.postValue(State.Loading())
            repository.getDailyCondition(lat, lon)
                .collect { response ->
                    if (response.resultType == ResultType.SUCCESS) {
                        dailyCondition.postValue(response.data)
                        stateDaily.postValue(State.Success())
                    } else {
                        Log.e("DAILY_RESPONSE", response.error?.message.toString())
                        stateDaily.postValue(State.Error())
                    }
                }
        }
    }


    fun saveLocation(location: LocationWithCoords): Long {
        var id = -10L
        viewModelScope.launch(Dispatchers.IO) {
            id = repository.saveNewLocation(location)
        }
        return id
    }

    fun deleteLocation(locationId: Long): Int {
        var id = -10
        viewModelScope.launch(Dispatchers.IO) {
            id = repository.deleteLocation(locationId)
        }
        return id
    }


    private fun mergeStates() {
        unionStates.addSource(stateCurrent) { unionStates.postValue(handleStates()) }
        unionStates.addSource(stateHourly) { unionStates.postValue(handleStates()) }
        unionStates.addSource(stateDaily) { unionStates.postValue(handleStates()) }
    }

    private fun handleStates(): State {
        if ((stateCurrent.value is State.Error) ||
            (stateHourly.value is State.Error) ||
            (stateDaily.value is State.Error)
        ) return State.Error()

        return if ((stateCurrent.value is State.Success) &&
            (stateHourly.value is State.Success) &&
            (stateDaily.value is State.Success)
        ) State.Success()
        else State.Loading()
    }
}