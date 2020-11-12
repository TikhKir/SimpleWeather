package com.example.simpleweather.network.locationiq

import com.example.simpleweather.network.locationiq.rawmodel.RawLocation
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationIqService {
    companion object {
        const val LOCATION_IQ_BASE_URL = "https://eu1.locationiq.com/v1/"

        private const val PARAMS_QUERY = "city"
        private const val PARAMS_LATITUDE = "lat"
        private const val PARAMS_LONGITUDE = "lon"
        private const val PARAMS_API_KEY = "key"
        private const val PARAMS_LANGUAGE = "accept-language"
        private const val PARAMS_NORMALIZED = "normalizeaddress"
        private const val PARAMS_LIMIT_RESPONSES = "limit"
        private const val PARAMS_FORMAT_RESPONSE = "format"
        private const val PARAMS_NORMALIZED_CITY = "normalizecity"
        private const val PARAMS_ADDRESS_DETAILS = "addressdetails"

        private const val API_KEY = "03a8b218ce9f80"
        private const val LANGUAGE_RU = "ru"
        private const val TRUE = 1 //todo: возможно поменять на boolean
        private const val DEF_LIMIT_RESPONSES = 20
        private const val FORMAT_JSON = "json"
    }

    @GET("search.php")
    suspend fun getCoordsListByCity(
        @Query(PARAMS_QUERY) cityName: String,
        @Query(PARAMS_NORMALIZED) normalizedValue: Int = TRUE,
        @Query(PARAMS_FORMAT_RESPONSE) formatValue: String = FORMAT_JSON,
        @Query(PARAMS_NORMALIZED_CITY) normalizedCityValue: Int = TRUE,
        @Query(PARAMS_LIMIT_RESPONSES) countResponses: Int = DEF_LIMIT_RESPONSES,
        @Query(PARAMS_LANGUAGE) languageValue: String = LANGUAGE_RU,
        @Query(PARAMS_ADDRESS_DETAILS) addressDetailsValue: Int = TRUE,
        @Query(PARAMS_API_KEY) apiKey: String = API_KEY
    ) : List<RawLocation>


    @GET("reverse.php") //this method must return a list with nearby locations, but API not support this feature =(
    suspend fun getCityListByCoords(
        @Query(PARAMS_LATITUDE) latitude: Float,
        @Query(PARAMS_LONGITUDE) longitude: Float,
        @Query(PARAMS_NORMALIZED) normalizedValue: Int = TRUE,
        @Query(PARAMS_FORMAT_RESPONSE) formatValue: String = FORMAT_JSON,
        @Query(PARAMS_NORMALIZED_CITY) normalizedCityValue: Int = TRUE,
        @Query(PARAMS_LIMIT_RESPONSES) countResponses: Int = DEF_LIMIT_RESPONSES,
        @Query(PARAMS_LANGUAGE) languageValue: String = LANGUAGE_RU,
        @Query(PARAMS_ADDRESS_DETAILS) addressDetailsValue: Int = TRUE,
        @Query(PARAMS_API_KEY) apiKey: String = API_KEY
    ) : RawLocation

}