package com.example.simpleweather.di

import android.content.Context
import com.example.simpleweather.utils.asyncunits.AsyncPreferencesUnitChanger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AsyncPreferencesUnitChangerModule {

    @Singleton
    @Provides
    fun provideAsyncPreferencesUnitChanger(@ApplicationContext context: Context): AsyncPreferencesUnitChanger {
        return AsyncPreferencesUnitChanger(context)
    }
}