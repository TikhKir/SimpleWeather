package com.example.simpleweather

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.simpleweather.repository.model.CurrentWeatherCondition
import com.example.simpleweather.utils.Constants
import com.example.simpleweather.utils.datawrappers.Result
import com.example.simpleweather.utils.datawrappers.ResultType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
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
            sendBlocking(newEmit)
        }
        preferencesManager.registerOnSharedPreferenceChangeListener(listener)
        awaitClose { preferencesManager.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    fun transformBySharedPrefUnits(
        result: Result<CurrentWeatherCondition>,
        sharedPref: MutableMap<String, *>
    ): Result<CurrentWeatherCondition> {
        return when (result.resultType) {
            ResultType.SUCCESS -> {
                var condition = result.data?.copy()
                sharedPref.forEach { prefValue ->
                    when (prefValue.key) {
                        Constants.SHARED_PREF_KEY_UNITS_DEGREE,
                        Constants.SHARED_PREF_KEY_UNITS_WIND_SPEED,
                        Constants.SHARED_PREF_KEY_UNITS_PRESSURE
                        -> condition = transformCurrentWithMap(condition!!, prefValue)
                    }
                }
                Result.success(condition)
            }
            ResultType.ERROR -> result
        }
    }

    private fun transformCurrentWithMap(
        condition: CurrentWeatherCondition,
        preferencesMap: Map.Entry<String, *>
    ): CurrentWeatherCondition {
        when (preferencesMap.value) {
            Constants.UNITS_FAHRENHEIT -> condition.temp = condition.temp!! * 1.8F + 32
            Constants.UNITS_WIND_KM_P_H -> condition.windSpeed = condition.windSpeed!! * 3.6F
            Constants.UNITS_PRESSURE_MM -> condition.pressure = (condition.pressure!! * 0.75F).roundToInt()
        }
        return condition
    }


}