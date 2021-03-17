package com.example.simpleweather.utils.asyncunits

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.simpleweather.repository.model.CurrentWeatherCondition
import com.example.simpleweather.repository.model.HourlyWeatherCondition
import com.example.simpleweather.ui.model.*
import com.example.simpleweather.utils.Constants
import com.example.simpleweather.utils.datawrappers.Result
import com.example.simpleweather.utils.datawrappers.ResultType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.math.roundToInt

class AsyncPreferencesUnitChanger(context: Context) {

    private val preferencesManager = PreferenceManager.getDefaultSharedPreferences(context)

    @ExperimentalCoroutinesApi
    suspend fun getPreferencesFlow(): Flow<MutableMap<String, *>> = callbackFlow {
        offer(preferencesManager.all) // first emit
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, _ ->
            val newEmit = sharedPreferences.all
            offer(newEmit)
        }
        preferencesManager.registerOnSharedPreferenceChangeListener(listener)
        awaitClose { preferencesManager.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    fun transformCurrentAccordingUnits(
        result: Result<CurrentWeatherCondition>,
        sharedPref: MutableMap<String, *>
    ): Result<CurrentConditionUI> {
        return when (result.resultType) {
            ResultType.SUCCESS -> {
                var condition = result.data?.toCurrentConditionUI()

                sharedPref.forEach { prefValue ->
                    when (prefValue.key) {
                        Constants.SHARED_PREF_KEY_UNITS_DEGREE,
                        Constants.SHARED_PREF_KEY_UNITS_WIND_SPEED,
                        Constants.SHARED_PREF_KEY_UNITS_PRESSURE
                        -> condition = transformCurrent(condition!!, prefValue)
                    }
                }
                Result.success(condition)
            }
            ResultType.ERROR -> Result.error(result.error)
        }
    }

    fun transformHourlyAccordingUnits(
        result: Result<List<HourlyWeatherCondition>>,
        sharedPref: MutableMap<String, *>
    ): Result<List<HourlyConditionUI>> {
        return when(result.resultType) {
            ResultType.SUCCESS -> {
                var conditionList = result.data?.map { it.toHourlyWeatherUI() }

                sharedPref.forEach { prefValue ->
                    when (prefValue.key) {
                        Constants.SHARED_PREF_KEY_UNITS_DEGREE,
                        Constants.SHARED_PREF_KEY_UNITS_WIND_SPEED,
                        Constants.SHARED_PREF_KEY_UNITS_PRESSURE
                        -> conditionList = transformHourly(conditionList!!, prefValue)
                    }
                }
                Result.success(conditionList)
            }
            ResultType.ERROR -> Result.error(result.error)
        }
    }

    private fun transformCurrent(
        condition: CurrentConditionUI,
        preferencesMap: Map.Entry<String, *>
    ): CurrentConditionUI {
        when (preferencesMap.value) {
            Constants.UNITS_FAHRENHEIT -> {
                condition.temp = (condition.temp * 1.8F + 32).roundToInt()
                condition.tempFL = (condition.tempFL * 1.8F + 32).roundToInt()
                condition.tempUnits = DegreeUnits.Fahrenheit
            }
            Constants.UNITS_WIND_KM_P_H -> {
                condition.windSpeed = (condition.windSpeed * 3.6F)
                condition.windSpeedUnits = WindSpeedUnits.KilometersPerHour
            }
            Constants.UNITS_PRESSURE_MM -> {
                condition.pressure = (condition.pressure * 0.75F).roundToInt()
                condition.pressureUnits = PressureUnits.MillimetersOfMercury
            }
        }
        return condition
    }

    private fun transformHourly(
        conditionList: List<HourlyConditionUI>,
        preferencesMap: Map.Entry<String, *>
    ): List<HourlyConditionUI> {
        conditionList.forEach { condition ->
            when (preferencesMap.value) {
                Constants.UNITS_FAHRENHEIT -> {
                    condition.temp = (condition.temp * 1.8F + 32).roundToInt()
                    condition.tempFL = (condition.tempFL * 1.8F + 32).roundToInt()
                    condition.tempUnits = DegreeUnits.Fahrenheit
                }
                Constants.UNITS_WIND_KM_P_H -> {
                    condition.windSpeed = (condition.windSpeed * 3.6F)
                    condition.windSpeedUnits = WindSpeedUnits.KilometersPerHour
                }
                Constants.UNITS_PRESSURE_MM -> {
                    condition.pressure = (condition.pressure * 0.75F).roundToInt()
                    condition.pressureUnits = PressureUnits.MillimetersOfMercury
                }
            }
        }
        return conditionList
    }
}