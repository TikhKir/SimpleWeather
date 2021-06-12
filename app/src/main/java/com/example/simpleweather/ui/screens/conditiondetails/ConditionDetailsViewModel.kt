package com.example.simpleweather.ui.screens.conditiondetails

import android.util.Log
import androidx.lifecycle.*
import com.example.simpleweather.domain.MeasureUnitsTransformUseCase
import com.example.simpleweather.domain.model.CurrentCondition
import com.example.simpleweather.domain.model.DailyCondition
import com.example.simpleweather.domain.model.HourlyCondition
import com.example.simpleweather.domain.model.Location
import com.example.simpleweather.utils.State
import com.example.simpleweather.utils.datawrappers.ResultType
import com.example.simpleweather.utils.favswitcher.DeferredFavouriteSwitcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConditionDetailsViewModel @Inject constructor(
    private val transformUseCase: MeasureUnitsTransformUseCase,
    private val deferredFavouriteSwitcher: DeferredFavouriteSwitcher
) : ViewModel() {

    private val currentCondition = MutableLiveData<CurrentCondition>()
    private val hourlyCondition = MutableLiveData<List<HourlyCondition>>()
    private val dailyCondition = MutableLiveData<List<DailyCondition>>()
    private val stateCurrent: MutableLiveData<State> = MutableLiveData(State.Default())
    private val stateHourly: MutableLiveData<State> = MutableLiveData(State.Default())
    private val stateDaily: MutableLiveData<State> = MutableLiveData(State.Default())
    private val unionStates = MediatorLiveData<State>()

    val currentLiveData: LiveData<CurrentCondition> get() = currentCondition
    val hourlyLiveData: LiveData<List<HourlyCondition>> get() = hourlyCondition
    val dailyLivaData: LiveData<List<DailyCondition>> get() = dailyCondition
    val stateLiveData: LiveData<State> get() = unionStates

    var favouriteLocation: Location? = null
    var isFavourite: Boolean = false

    init {
        mergeStates()
    }

    @ExperimentalCoroutinesApi
    fun getHourlyWeatherCondition(locationId: Long) = viewModelScope.launch {
        transformUseCase.getHourly(locationId)
            .collect { response ->
                if (response.resultType == ResultType.SUCCESS) {
                    hourlyCondition.postValue(response.data!!)
                    stateHourly.postValue(State.Success())
                } else {
                    Log.e("HOURLY_RESPONSE", response.error?.message.toString())
                    stateHourly.postValue(State.Error(response.error?.message.toString()))
                }
            }
    }


    @ExperimentalCoroutinesApi
    fun getCurrentWeatherCondition(locationId: Long) = viewModelScope.launch {
        transformUseCase.getCurrent(locationId)
            .collect { response ->
                if (response.resultType == ResultType.SUCCESS) {
                    currentCondition.postValue(response.data!!)
                    stateCurrent.postValue(State.Success())
                } else {
                    Log.e("CURRENT_RESPONSE", response.error?.message.toString())
                    stateCurrent.postValue(State.Error(response.error?.message.toString()))
                }
            }
    }


    @ExperimentalCoroutinesApi
    fun getDailyWeatherCondition(locationId: Long) = viewModelScope.launch {
        transformUseCase.getDaily(locationId)
            .collect { response ->
                if (response.resultType == ResultType.SUCCESS) {
                    dailyCondition.postValue(response.data!!)
                    stateDaily.postValue(State.Success())
                } else {
                    Log.e("DAILY_RESPONSE", response.error?.message.toString())
                    stateDaily.postValue(State.Error(response.error?.message.toString()))
                }
            }
    }


    @ExperimentalCoroutinesApi
    fun getHourlyWeatherCondition(lat: Float, lon: Float) = viewModelScope.launch {
        transformUseCase.getHourly(lat, lon)
            .collect { response ->
                if (response.resultType == ResultType.SUCCESS) {
                    hourlyCondition.postValue(response.data!!)
                    stateHourly.postValue(State.Success())
                } else {
                    Log.e("HOURLY_RESPONSE", response.error?.message.toString())
                    stateHourly.postValue(State.Error(response.error?.message.toString()))
                }
            }
    }


    @ExperimentalCoroutinesApi
    fun getCurrentWeatherCondition(lat: Float, lon: Float) = viewModelScope.launch {
        transformUseCase.getCurrent(lat, lon)
            .collect { response ->
                if (response.resultType == ResultType.SUCCESS) {
                    currentCondition.postValue(response.data!!)
                    stateCurrent.postValue(State.Success())
                } else {
                    Log.e("CURRENT_RESPONSE", response.error?.message.toString())
                    stateCurrent.postValue(State.Error(response.error?.message.toString()))
                }
            }
    }


    @ExperimentalCoroutinesApi
    fun getDailyWeatherCondition(lat: Float, lon: Float) = viewModelScope.launch {
        transformUseCase.getDaily(lat, lon)
            .collect { response ->
                if (response.resultType == ResultType.SUCCESS) {
                    dailyCondition.postValue(response.data!!)
                    stateDaily.postValue(State.Success())
                } else {
                    Log.e("DAILY_RESPONSE", response.error?.message.toString())
                    stateDaily.postValue(State.Error(response.error?.message.toString()))
                }
            }
    }


    private fun mergeStates() = viewModelScope.launch {
        unionStates.addSource(stateCurrent) { unionStates.value = handleStates() }
        unionStates.addSource(stateHourly) { unionStates.value = handleStates() }
        unionStates.addSource(stateDaily) { unionStates.value = handleStates() }
    }

    private fun handleStates(): State {
        if ((stateCurrent.value is State.Error) ||
            (stateHourly.value is State.Error) ||
            (stateDaily.value is State.Error)
        ) return State.Error("Что-то пошло не так...")

        return if ((stateCurrent.value is State.Success) &&
            (stateHourly.value is State.Success) &&
            (stateDaily.value is State.Success)
        ) State.Success()
        else State.Loading()
    }

    private fun saveLocation(location: Location) {
        deferredFavouriteSwitcher.addToFavourite(location)
    }

    private fun deleteLocation(locationId: Long) {
        deferredFavouriteSwitcher.deleteFromFavourite(locationId)
    }

    override fun onCleared() {
        super.onCleared()
        if (favouriteLocation != null && isFavourite) {
            saveLocation(favouriteLocation!!)
        } else {
            deleteLocation(favouriteLocation!!.locationId)
        }
    }
}