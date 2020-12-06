package com.example.simpleweather.local

import android.util.Log
import com.example.simpleweather.repository.model.CurrentWeatherCondition
import com.example.simpleweather.repository.model.DailyWeatherCondition
import com.example.simpleweather.repository.model.HourlyWeatherCondition
import com.example.simpleweather.repository.model.LocationWithCoords
import com.example.simpleweather.utils.datawrappers.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.threeten.bp.Instant
import org.threeten.bp.temporal.ChronoUnit.HOURS
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DataApiImpl @Inject constructor(
    private val weatherDao: WeatherDao
): DataApi {

    override suspend fun getDailyForecast(locationId: Long): Flow<List<HourlyWeatherCondition>> {
        TODO("получать только свежие данные не раньше нужного таймстампа")
    }

    override suspend fun getHourlyForecast(locationId: Long): Flow<Result<List<HourlyWeatherCondition>>> {
        val currentTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
        return weatherDao.getHourlyForecast(locationId, currentTime)
            .map { listConditionDB ->
                listConditionDB.map {
                    it.toHourlyWeatherCondition()
                }
            }
            .map { Result.success(it) }
            .catch { e -> if (e is Exception) emit(Result.error(e)) }
    }

    override suspend fun getCurrentForecast(locationId: Long): Flow<Result<CurrentWeatherCondition>> {
        val currentTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
        val truncatedToHours = Instant.now().truncatedTo(HOURS).epochSecond
        Log.e("TIMES", "$currentTime  $truncatedToHours")

        return weatherDao.getCurrentForecast(locationId, truncatedToHours)
            .map { it.toCurrentWeatherCondition() }
            .map { Result.success(it) }
            .catch { e -> if (e is Exception) emit(Result.error(e)) }
    }

    override suspend fun saveDailyForecast(locationId: Long, listDaily: List<DailyWeatherCondition>) {
        val transformedList = listDaily
            .map { it.toDailyWeatherConditionDB(locationId) }
        weatherDao.saveDailyForecast(transformedList)
        }




    override suspend fun saveHourlyForecast(locationId: Long, listHourly: List<HourlyWeatherCondition>) {
        val transformedList = listHourly
            .map { it.toHourlyWeatherConditionDB(locationId) }
        weatherDao.saveHourlyForecast(transformedList)
    }




    override suspend fun saveNewLocation(location: LocationWithCoords): Long {
        return weatherDao.saveNewLocation(location.toLocationDB())
    }

    override suspend fun getSavedLocations(): Flow<List<LocationWithCoords>> {
        return weatherDao.getSavedLocations()
            .map {
            it.map { locationDB ->
                locationDB.toLocationWithCoords()
            }
        }
    }

    override suspend fun getSavedLocationById(locationId: Long): LocationWithCoords {
        return weatherDao.getSavedLocationById(locationId)
            .toLocationWithCoords()
    }

    override suspend fun deleteLocation(locationId: Long): Int {
        return weatherDao.deleteLocation(locationId)
    }




    override suspend fun removeGarbage() {
        val currentTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
        TODO("здесь тоже нужно отправлять таймстамп")
    }

}