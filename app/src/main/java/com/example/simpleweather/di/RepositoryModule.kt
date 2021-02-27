package com.example.simpleweather.di

import com.example.simpleweather.local.DataApi
import com.example.simpleweather.network.locationiq.LocationIqApi
import com.example.simpleweather.network.openweather.OpenWeatherApi
import com.example.simpleweather.repository.RepositoryApi
import com.example.simpleweather.repository.RepositoryApiImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun providesRepository(locationIqApi: LocationIqApi, openWeatherApi: OpenWeatherApi, dataApi: DataApi) : RepositoryApi {
        return RepositoryApiImpl(openWeatherApi, locationIqApi, dataApi)
    }
}