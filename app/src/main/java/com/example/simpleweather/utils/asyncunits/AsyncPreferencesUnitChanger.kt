package com.example.simpleweather.utils.asyncunits

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.simpleweather.domain.model.CurrentCondition
import com.example.simpleweather.domain.model.DailyCondition
import com.example.simpleweather.domain.model.HourlyCondition
import com.example.simpleweather.utils.datawrappers.Result
import com.example.simpleweather.utils.datawrappers.ResultType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlin.math.roundToInt

@ExperimentalCoroutinesApi
class AsyncPreferencesUnitChanger(context: Context) {
    private val preferencesManager = PreferenceManager.getDefaultSharedPreferences(context)
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    private val preferencesFlow = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, _ ->
            val newEmit = sharedPreferences.all
            offer(newEmit)
        }
        preferencesManager.registerOnSharedPreferenceChangeListener(listener)
        awaitClose { preferencesManager.unregisterOnSharedPreferenceChangeListener(listener) }
    }.stateIn(
        scope,
        SharingStarted.Lazily,
        preferencesManager.all //first emit
    )

    fun getPreferencesFlow(): StateFlow<MutableMap<String, *>> = preferencesFlow

    fun transformToCurrentUIAccordingUnits(
        result: Result<CurrentCondition>,
        sharedPref: MutableMap<String, *>
    ): Result<CurrentCondition> {
        return when (result.resultType) {
            ResultType.SUCCESS -> {
                var condition = result.data?.copy()
                //todo: разобраться что тут происходит вообще

                sharedPref.forEach { prefValue ->
                    when (prefValue.key) {
                        DegreeUnits.key,
                        WindSpeedUnits.key,
                        PressureUnits.key
                        -> condition = transformCurrent(condition!!, prefValue)
                    }
                }
                Result.success(condition)
            }
            ResultType.ERROR -> Result.error(result.error)
        }
    }

    fun transformToHourlyUIAccordingUnits(
        result: Result<List<HourlyCondition>>,
        sharedPref: MutableMap<String, *>
    ): Result<List<HourlyCondition>> {
        return when (result.resultType) {
            ResultType.SUCCESS -> {
                var conditionList = result.data?.map { it.copy() }

                sharedPref.forEach { prefValue ->
                    when (prefValue.key) {
                        DegreeUnits.key,
                        WindSpeedUnits.key,
                        PressureUnits.key
                        -> conditionList = transformHourly(conditionList!!, prefValue)
                    }
                }
                Result.success(conditionList)
            }
            ResultType.ERROR -> Result.error(result.error)
        }
    }

    fun transformToDailyUIAccordingUnits(
        result: Result<List<DailyCondition>>,
        sharedPref: MutableMap<String, *>
    ): Result<List<DailyCondition>> {
        return when (result.resultType) {
            ResultType.SUCCESS -> {
                var conditionList = result.data?.map { it.copy() }

                sharedPref.forEach { prefValue ->
                    when (prefValue.key) {
                        DegreeUnits.key, WindSpeedUnits.key, PressureUnits.key ->
                            conditionList = transformDaily(conditionList!!, prefValue)
                    }
                }
                Result.success(conditionList)
            }
            ResultType.ERROR -> Result.error(result.error)
        }
    }

    private fun transformCurrent(
        condition: CurrentCondition,
        preferencesMap: Map.Entry<String, *>
    ): CurrentCondition {
        when (preferencesMap.value) {
            DegreeUnits.Fahrenheit.key -> condition.apply {
                temp = temp.celsiusToFahrenheit()
                tempFL = tempFL.celsiusToFahrenheit()
                tempUnits = DegreeUnits.Fahrenheit
            }
            WindSpeedUnits.KilometersPerHour.key -> condition.apply {
                windSpeed = windSpeed.kmPerHourToMetersPerSecond()
                windSpeedUnits = WindSpeedUnits.KilometersPerHour
            }
            PressureUnits.MillimetersOfMercury.key -> condition.apply {
                pressure = pressure.hPaToMillimeters()
                pressureUnits = PressureUnits.MillimetersOfMercury
            }
        }
        return condition
    }

    private fun transformHourly(
        conditionList: List<HourlyCondition>,
        preferencesMap: Map.Entry<String, *>
    ): List<HourlyCondition> {
        conditionList.forEach { condition ->
            when (preferencesMap.value) {
                DegreeUnits.Fahrenheit.key -> condition.apply {
                    temp = temp.celsiusToFahrenheit()
                    tempFL = tempFL.celsiusToFahrenheit()
                    tempUnits = DegreeUnits.Fahrenheit
                }
                WindSpeedUnits.KilometersPerHour.key -> condition.apply {
                    windSpeed = windSpeed.kmPerHourToMetersPerSecond()
                    windSpeedUnits = WindSpeedUnits.KilometersPerHour
                }
                PressureUnits.MillimetersOfMercury.key -> condition.apply {
                    pressure = pressure.hPaToMillimeters()
                    pressureUnits = PressureUnits.MillimetersOfMercury
                }
            }
        }
        return conditionList
    }

    private fun transformDaily(
        conditionList: List<DailyCondition>,
        preferencesMap: Map.Entry<String, *>
    ): List<DailyCondition> {
        conditionList.forEach { condition ->
            when (preferencesMap.value) {
                DegreeUnits.Fahrenheit.key -> condition.apply {
                    tempDay = tempDay.celsiusToFahrenheit()
                    tempMorning = tempMorning.celsiusToFahrenheit()
                    tempEvening = tempEvening.celsiusToFahrenheit()
                    tempNight = tempNight.celsiusToFahrenheit()
                    tempMorningFL = tempMorningFL.celsiusToFahrenheit()
                    tempDayFL = tempDayFL.celsiusToFahrenheit()
                    tempEveningFL = tempEveningFL.celsiusToFahrenheit()
                    tempNightFL = tempNightFL.celsiusToFahrenheit()
                    tempMax = tempMax.celsiusToFahrenheit()
                    tempMin = tempMin.celsiusToFahrenheit()
                    tempUnits = DegreeUnits.Fahrenheit
                }
                PressureUnits.HectoPascals.key -> condition.apply {
                    pressure = pressure.hPaToMillimeters()
                    pressureUnits = PressureUnits.MillimetersOfMercury
                }
                WindSpeedUnits.KilometersPerHour.key -> condition.apply {
                    windSpeed = windSpeed.kmPerHourToMetersPerSecond()
                    windSpeedUnits = WindSpeedUnits.KilometersPerHour
                }
            }
        }
        return conditionList
    }


    private fun Int.celsiusToFahrenheit(): Int = (this * 1.8F + 32).roundToInt()
    private fun Int.hPaToMillimeters(): Int = (this * 0.75F).roundToInt()
    private fun Float.kmPerHourToMetersPerSecond(): Float = (this * 3.6F)
}