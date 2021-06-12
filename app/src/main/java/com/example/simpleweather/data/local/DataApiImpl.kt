package com.example.simpleweather.data.local

import com.example.simpleweather.domain.datawrappers.Result
import com.example.simpleweather.domain.model.CurrentCondition
import com.example.simpleweather.domain.model.DailyCondition
import com.example.simpleweather.domain.model.HourlyCondition
import com.example.simpleweather.domain.model.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.threeten.bp.Instant
import org.threeten.bp.temporal.ChronoUnit.HOURS
import javax.inject.Inject

class DataApiImpl @Inject constructor(
    private val weatherDao: WeatherDao
) : DataApi {

    override suspend fun getDailyForecast(locationId: Long): Flow<Result<List<DailyCondition>>> =
        withContext(Dispatchers.IO) {
            weatherDao.getDailyForecast(locationId, currentTime())
                .map { listOfConditionDB ->
                    listOfConditionDB.map { it.toDailyCondition() }
                }
                .map { Result.success(it) }
                .catch { e -> if (e is Exception) emit(Result.error(e)) }
        }

    override suspend fun getHourlyForecast(locationId: Long): Flow<Result<List<HourlyCondition>>> =
        withContext(Dispatchers.IO) {
            weatherDao.getHourlyForecast(locationId, currentTime())
                .map { listConditionDB ->
                    listConditionDB.map { it.toHourlyCondition() }
                }
                .map { Result.success(it) }
                .catch { e -> if (e is Exception) emit(Result.error(e)) }
        }

    override suspend fun getCurrentForecast(locationId: Long): Flow<Result<CurrentCondition>> =
        withContext(Dispatchers.IO) {
            weatherDao.getCurrentForecast(locationId, currentHours())
                .map { it.toCurrentCondition() }
                .map { Result.success(it) }
                .catch { e -> if (e is Exception) emit(Result.error(e)) }
        }

    override suspend fun saveDailyForecast(locationId: Long, listDaily: List<DailyCondition>) =
        withContext(Dispatchers.IO) {
            val transformedList = listDaily.map { it.toDailyConditionDB(locationId) }
            weatherDao.saveDailyForecast(transformedList)
        }


    override suspend fun saveHourlyForecast(locationId: Long, listHourly: List<HourlyCondition>) =
        withContext(Dispatchers.IO) {
            val transformedList = listHourly
                .map { it.toHourlyConditionDB(locationId) }
            weatherDao.saveHourlyForecast(transformedList)
        }


    override suspend fun saveNewLocation(location: Location): Long =
        withContext(Dispatchers.IO) {
            weatherDao.saveNewLocation(location.toLocationDB())
        }

    override suspend fun updateDailyRefreshTime(locationId: Long) =
        withContext(Dispatchers.IO) {
            weatherDao.updateDailyRefreshTime(currentTime(), locationId)
        }

    override suspend fun updateHourlyRefreshTime(locationId: Long) =
        withContext(Dispatchers.IO) {
            weatherDao.updateHourlyRefreshTime(currentTime(), locationId)
        }

    override suspend fun updateCurrentRefreshTime(locationId: Long) =
        withContext(Dispatchers.IO) {
            weatherDao.updateCurrentRefreshTime(currentTime(), locationId)
        }

    override suspend fun getSavedLocations(): Flow<List<Location>> =
        withContext(Dispatchers.IO) {
            weatherDao.getSavedLocations()
                .map {
                    it.map { locationDB ->
                        locationDB.toLocation()
                    }
                }
        }

    override suspend fun getSavedLocationsSync(): List<Location> =
        withContext(Dispatchers.IO) {
            weatherDao.getSavedLocationsSync()
                .map { it.toLocation() }
        }

    override suspend fun getSavedLocationById(locationId: Long): Location =
        withContext(Dispatchers.IO) {
            weatherDao.getSavedLocationById(locationId)
                .toLocation()
        }

    override suspend fun deleteLocation(locationId: Long): Int =
        withContext(Dispatchers.IO) {
            weatherDao.deleteLocation(locationId)
        }


    override suspend fun removeGarbage() = withContext(Dispatchers.IO) {
        //val currentTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
        TODO("здесь тоже нужно отправлять таймстамп")
    }

    private fun currentTime() = Instant.now().epochSecond
    private fun currentHours() = Instant.now().truncatedTo(HOURS).epochSecond
}