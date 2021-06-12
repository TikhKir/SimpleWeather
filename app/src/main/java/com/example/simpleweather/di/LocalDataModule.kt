package com.example.simpleweather.di

import android.content.Context
import androidx.room.Room
import com.example.simpleweather.data.local.DataApi
import com.example.simpleweather.data.local.DataApiImpl
import com.example.simpleweather.data.local.WeatherDao
import com.example.simpleweather.data.local.WeatherDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): WeatherDataBase {
        return Room.databaseBuilder(context, WeatherDataBase::class.java, WeatherDataBase.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provide(database: WeatherDataBase): WeatherDao {
        return database.weatherDao()
    }

    @Singleton
    @Provides
    fun provideDataApi(weatherDao: WeatherDao): DataApi {
        return DataApiImpl(weatherDao)
    }

}