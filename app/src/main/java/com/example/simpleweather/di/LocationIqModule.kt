package com.example.simpleweather.di

import com.example.simpleweather.network.locationiq.LocationIqApi
import com.example.simpleweather.network.locationiq.LocationIqApiImpl
import com.example.simpleweather.network.locationiq.LocationIqService
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
object LocationIqModule {

    @Singleton
    @Provides
    @Named("LocationIqRetrofit")
    fun provideLocationIqRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(LocationIqService.LOCATION_IQ_BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    fun provideLocationIqService(@Named("LocationIqRetrofit") retrofit: Retrofit): LocationIqService {
        return retrofit.create(LocationIqService::class.java)
    }

    @Singleton
    @Provides
    fun providesLocationIqApi(locationIqService: LocationIqService): LocationIqApi {
        return LocationIqApiImpl(locationIqService)
    }

}