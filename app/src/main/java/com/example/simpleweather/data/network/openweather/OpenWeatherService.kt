package com.example.simpleweather.data.network.openweather

import com.example.simpleweather.BuildConfig
import com.example.simpleweather.data.network.openweather.rawmodel.RawOneCallResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherService {

    companion object {
        const val OPEN_WEATHER_BASE_URL = "https://api.openweathermap.org/data/2.5/"

        private const val PARAM_LATITUDE = "lat"
        private const val PARAM_LONGITUDE = "lon"
        private const val PARAM_EXCLUDE = "exclude"
        private const val PARAM_API_KEY = "appid"
        private const val PARAM_LANGUAGE = "lang"
        private const val PARAM_UNITS = "units"

        private const val LANGUAGE_RU = "ru"
        private const val API_KEY = BuildConfig.API_KEY_OPENWEATHER
        private const val EXCLUDE_HOURLY = "hourly"
        private const val EXCLUDE_DAILY = "daily"
        private const val EXCLUDE_ALERTS = "alerts"
        private const val EXCLUDE_CURRENT = "current"
        private const val EXCLUDE_MINUTELY = "minutely"
        private const val UNITS_METRIC = "metric" //dont change query units!
        private const val UNITS_IMPERIAL = "imperial" //dont change query units!

    }

    @GET("onecall")
    suspend fun getAllForecastByCoord(
        @Query(PARAM_LATITUDE) latitude: Float,
        @Query(PARAM_LONGITUDE) longitude: Float,
        @Query(PARAM_EXCLUDE) exclude: String = "$EXCLUDE_MINUTELY, $EXCLUDE_ALERTS",
        @Query(PARAM_LANGUAGE) language: String = LANGUAGE_RU,
        @Query(PARAM_UNITS) units: String = UNITS_METRIC,
        @Query(PARAM_API_KEY) apiKey: String = API_KEY
    ) : RawOneCallResponse

    @GET("onecall")
    suspend fun getDailyForecastByCoord(
        @Query(PARAM_LATITUDE) latitude: Float,
        @Query(PARAM_LONGITUDE) longitude: Float,
        @Query(PARAM_EXCLUDE) exclude: String = "$EXCLUDE_MINUTELY,$EXCLUDE_HOURLY,$EXCLUDE_CURRENT, $EXCLUDE_ALERTS",
        @Query(PARAM_LANGUAGE) language: String = LANGUAGE_RU,
        @Query(PARAM_UNITS) units: String = UNITS_METRIC,
        @Query(PARAM_API_KEY) apiKey: String = API_KEY
    ) : RawOneCallResponse

    @GET("onecall")
    suspend fun getCurrentlyForecastByCoord(
        @Query(PARAM_LATITUDE) latitude: Float,
        @Query(PARAM_LONGITUDE) longitude: Float,
        @Query(PARAM_EXCLUDE) exclude: String = "$EXCLUDE_MINUTELY,$EXCLUDE_HOURLY,$EXCLUDE_DAILY, $EXCLUDE_ALERTS",
        @Query(PARAM_LANGUAGE) language: String = LANGUAGE_RU,
        @Query(PARAM_UNITS) units: String = UNITS_METRIC,
        @Query(PARAM_API_KEY) apiKey: String = API_KEY
    ) : RawOneCallResponse

    @GET("onecall")
    suspend fun getHourlyForecastByCoord(
        @Query(PARAM_LATITUDE) latitude: Float,
        @Query(PARAM_LONGITUDE) longitude: Float,
        @Query(PARAM_EXCLUDE) exclude: String = "$EXCLUDE_MINUTELY,$EXCLUDE_DAILY,$EXCLUDE_DAILY, $EXCLUDE_ALERTS",
        @Query(PARAM_LANGUAGE) language: String = LANGUAGE_RU,
        @Query(PARAM_UNITS) units: String = UNITS_METRIC,
        @Query(PARAM_API_KEY) apiKey: String = API_KEY
    ) : RawOneCallResponse
}