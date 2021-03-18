package com.example.simpleweather.utils.asyncunits

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.simpleweather.repository.model.CurrentWeatherCondition
import com.example.simpleweather.repository.model.DailyWeatherCondition
import com.example.simpleweather.repository.model.HourlyWeatherCondition
import com.example.simpleweather.ui.model.CurrentConditionUI
import com.example.simpleweather.ui.model.DailyConditionUI
import com.example.simpleweather.ui.model.HourlyConditionUI
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

    fun transformToCurrentUIAccordingUnits(
        result: Result<CurrentWeatherCondition>,
        sharedPref: MutableMap<String, *>
    ): Result<CurrentConditionUI> {
        return when (result.resultType) {
            ResultType.SUCCESS -> {
                var condition = result.data?.toCurrentConditionUI()

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
        result: Result<List<HourlyWeatherCondition>>,
        sharedPref: MutableMap<String, *>
    ): Result<List<HourlyConditionUI>> {
        return when (result.resultType) {
            ResultType.SUCCESS -> {
                var conditionList = result.data?.map { it.toHourlyWeatherUI() }

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
        result: Result<List<DailyWeatherCondition>>,
        sharedPref: MutableMap<String, *>
    ): Result<List<DailyConditionUI>> {
        return when (result.resultType) {
            ResultType.SUCCESS -> {
                var conditionList = result.data?.map { it.toDailyConditionUI() }

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
        condition: CurrentConditionUI,
        preferencesMap: Map.Entry<String, *>
    ): CurrentConditionUI {
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
        conditionList: List<HourlyConditionUI>,
        preferencesMap: Map.Entry<String, *>
    ): List<HourlyConditionUI> {
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
        conditionList: List<DailyConditionUI>,
        preferencesMap: Map.Entry<String, *>
    ): List<DailyConditionUI> {
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