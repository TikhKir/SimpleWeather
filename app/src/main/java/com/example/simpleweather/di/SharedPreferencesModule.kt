package com.example.simpleweather.di

import android.content.Context
import com.example.simpleweather.sharedpreferences.SharedPreferencesSource
import com.example.simpleweather.sharedpreferences.SharedPreferencesSourceImpl
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {

    fun provideSharedPreferencesSource(@ApplicationContext context: Context): SharedPreferencesSource
        = SharedPreferencesSourceImpl(context)

}