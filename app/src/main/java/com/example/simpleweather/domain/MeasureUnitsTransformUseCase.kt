package com.example.simpleweather.domain

import com.example.simpleweather.data.sharedpreferences.SharedPreferencesSource
import com.example.simpleweather.domain.datawrappers.Result
import com.example.simpleweather.domain.model.*
import com.example.simpleweather.repository.RepositoryApi
import com.example.simpleweather.utils.datawrappers.ResultType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.roundToInt

class MeasureUnitsTransformUseCase @Inject constructor(
    private val repo: RepositoryApi,
    private val preferences: SharedPreferencesSource
) {

    suspend fun getCurrent(locationId: Long): Flow<Result<CurrentCondition>> =
        withContext(Dispatchers.Default) {
            repo.getCurrentCondition(locationId)
                .combine(preferences.getSharedPreferences()) { currentResult,
                                                               sharedPref ->
                    combineCurrentWithUnits(currentResult, sharedPref)
                }
        }

    suspend fun getCurrent(lat: Float, lon: Float): Flow<Result<CurrentCondition>> =
        withContext(Dispatchers.Default) {
            repo.getCurrentConditionWithoutCaching(lat, lon)
                .combine(preferences.getSharedPreferences()) { currentResult,
                                                               sharedPref ->
                    combineCurrentWithUnits(currentResult, sharedPref)
                }
        }

    suspend fun getHourly(locationId: Long): Flow<Result<List<HourlyCondition>>> =
        withContext(Dispatchers.Default) {
            repo.getHourlyCondition(locationId)
                .combine(preferences.getSharedPreferences()) { hourlyResult,
                                                               sharedPref ->
                    combineHourlyWithUnits(hourlyResult, sharedPref)
                }
        }

    suspend fun getHourly(lat: Float, lon: Float): Flow<Result<List<HourlyCondition>>> =
        withContext(Dispatchers.Default) {
            repo.getHourlyConditionWithoutCaching(lat, lon)
                .combine(preferences.getSharedPreferences()) { hourlyResult,
                                                               sharedPref ->
                    combineHourlyWithUnits(hourlyResult, sharedPref)
                }
        }

    suspend fun getDaily(locationId: Long): Flow<Result<List<DailyCondition>>> =
        withContext(Dispatchers.Default) {
            repo.getDailyCondition(locationId)
                .combine(preferences.getSharedPreferences()) { dailyResult,
                                                               sharedPref ->
                    combineDailyWithUnits(dailyResult, sharedPref)
                }
        }

    suspend fun getDaily(lat: Float, lon: Float): Flow<Result<List<DailyCondition>>> =
        withContext(Dispatchers.Default) {
            repo.getDailyConditionWithoutCaching(lat, lon)
                .combine(preferences.getSharedPreferences()) { dailyResult,
                                                               sharedPref ->
                    combineDailyWithUnits(dailyResult, sharedPref)
                }
        }


    private fun combineCurrentWithUnits(
        result: Result<CurrentCondition>,
        sharedPref: MutableMap<String, *>
    ): Result<CurrentCondition> {
        return when (result.resultType) {
            ResultType.SUCCESS -> {
                var condition = result.data?.copy()

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

    private fun combineHourlyWithUnits(
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

    private fun combineDailyWithUnits(
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