package com.example.simpleweather.di

import com.example.simpleweather.network.openweather.OpenWeatherApi
import com.example.simpleweather.network.openweather.OpenWeatherApiImpl
import com.example.simpleweather.network.openweather.OpenWeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OpenWeatherModule {

    @Singleton
    @Provides
    @Named("OpenWeatherRetrofit")
    fun providesOpenWeatherRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(OpenWeatherService.OPEN_WEATHER_BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    fun providesOpenWeatherService(@Named("OpenWeatherRetrofit") retrofit: Retrofit): OpenWeatherService {
        return retrofit.create(OpenWeatherService::class.java)
    }

    @Singleton
    @Provides
    fun providesOpenWeatherApi(openWeatherService: OpenWeatherService): OpenWeatherApi {
        return OpenWeatherApiImpl(openWeatherService)
    }
}