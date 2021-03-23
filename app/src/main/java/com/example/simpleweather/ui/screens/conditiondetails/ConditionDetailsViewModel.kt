package com.example.simpleweather.ui.screens.conditiondetails

import android.util.Log
import androidx.lifecycle.*
import com.example.simpleweather.repository.RepositoryApi
import com.example.simpleweather.repository.model.LocationWithCoords
import com.example.simpleweather.ui.model.CurrentConditionUI
import com.example.simpleweather.ui.model.DailyConditionUI
import com.example.simpleweather.ui.model.HourlyConditionUI
import com.example.simpleweather.utils.asyncunits.AsyncPreferencesUnitChanger
import com.example.simpleweather.utils.datawrappers.ResultType
import com.example.simpleweather.utils.datawrappers.State
import com.example.simpleweather.utils.favswitcher.DeferredFavouriteSwitcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConditionDetailsViewModel @Inject constructor(
    private val repository: RepositoryApi,
    private val asyncUnitChanger: AsyncPreferencesUnitChanger,
    private val deferredFavouriteSwitcher: DeferredFavouriteSwitcher
) : ViewModel() {

    private val currentCondition = MutableLiveData<CurrentConditionUI>()
    private val hourlyCondition = MutableLiveData<List<HourlyConditionUI>>()
    private val dailyCondition = MutableLiveData<List<DailyConditionUI>>()
    private val stateCurrent: MutableLiveData<State> = MutableLiveData(State.Default())
    private val stateHourly: MutableLiveData<State> = MutableLiveData(State.Default())
    private val stateDaily: MutableLiveData<State> = MutableLiveData(State.Default())
    private val unionStates = MediatorLiveData<State>()

    val currentLiveData: LiveData<CurrentConditionUI> get() = currentCondition
    val hourlyLiveData: LiveData<List<HourlyConditionUI>> get() = hourlyCondition
    val dailyLivaData: LiveData<List<DailyConditionUI>> get() = dailyCondition
    val stateLiveData: LiveData<State> get() = unionStates

    var favouriteLocation: LocationWithCoords? = null
    var isFavourite: Boolean = false

    init {
        mergeStates()
    }


    @ExperimentalCoroutinesApi
    fun getHourlyWeatherCondition(locationId: Long) = viewModelScope.launch(Dispatchers.IO) {
        val hourlyFlow = repository.getHourlyCondition(locationId)
        val sharedPrefFLow = asyncUnitChanger.getPreferencesFlow()

        hourlyFlow.combine(sharedPrefFLow) { hourlyResult,
                                             sharedPref ->
            asyncUnitChanger.transformToHourlyUIAccordingUnits(hourlyResult, sharedPref)
        }
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
    fun getCurrentWeatherCondition(locationId: Long) = viewModelScope.launch(Dispatchers.IO) {
        val currentFlow = repository.getCurrentCondition(locationId)
        val sharedPrefFLow = asyncUnitChanger.getPreferencesFlow()

        currentFlow.combine(sharedPrefFLow) { currentResult,
                                              sharedPref ->
            asyncUnitChanger.transformToCurrentUIAccordingUnits(currentResult, sharedPref)
        }
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
    fun getDailyWeatherCondition(locationId: Long) = viewModelScope.launch(Dispatchers.IO) {
        val dailyFlow = repository.getDailyCondition(locationId)
        val sharedPrefFLow = asyncUnitChanger.getPreferencesFlow()

        dailyFlow.combine(sharedPrefFLow) { dailyResult,
                                            sharedPref ->
            asyncUnitChanger.transformToDailyUIAccordingUnits(dailyResult, sharedPref)
        }
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
    fun getHourlyWeatherCondition(lat: Float, lon: Float) = viewModelScope.launch(Dispatchers.IO) {
        val hourlyFlow = repository.getHourlyConditionWithoutCaching(lat, lon)
        val sharedPrefFLow = asyncUnitChanger.getPreferencesFlow()

        hourlyFlow.combine(sharedPrefFLow) { hourlyResult,
                                             sharedPref ->
            asyncUnitChanger.transformToHourlyUIAccordingUnits(hourlyResult, sharedPref)
        }
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
    fun getCurrentWeatherCondition(lat: Float, lon: Float) = viewModelScope.launch(Dispatchers.IO) {
        val currentFlow = repository.getCurrentConditionWithoutCaching(lat, lon)
        val sharedPrefFlow = asyncUnitChanger.getPreferencesFlow()

        currentFlow.combine(sharedPrefFlow) { currentResult, sharedPref ->
            asyncUnitChanger.transformToCurrentUIAccordingUnits(currentResult, sharedPref)
        }
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
    fun getDailyWeatherCondition(lat: Float, lon: Float) = viewModelScope.launch(Dispatchers.IO) {
        val dailyFlow = repository.getDailyConditionWithoutCaching(lat, lon)
        val sharedPrefFLow = asyncUnitChanger.getPreferencesFlow()

        dailyFlow.combine(sharedPrefFLow) { dailyResult,
                                            sharedPref ->
            asyncUnitChanger.transformToDailyUIAccordingUnits(dailyResult, sharedPref)
        }
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

    private fun saveLocation(location: LocationWithCoords) {
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