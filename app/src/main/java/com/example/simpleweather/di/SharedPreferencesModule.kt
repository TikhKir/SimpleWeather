package com.example.simpleweather.di

import android.content.Context
import com.example.simpleweather.data.sharedpreferences.SharedPreferencesSource
import com.example.simpleweather.data.sharedpreferences.SharedPreferencesSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {

    @Singleton
    @Provides
    fun provideSharedPreferencesSource(@ApplicationContext context: Context): SharedPreferencesSource
        = SharedPreferencesSourceImpl(context)

}