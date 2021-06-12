package com.example.simpleweather.di.usecase

import com.example.simpleweather.data.sharedpreferences.SharedPreferencesSource
import com.example.simpleweather.domain.MeasureUnitsTransformUseCase
import com.example.simpleweather.repository.RepositoryApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MeasureUnitsTransformModule {

    @Singleton
    @Provides
    fun provideUnitsTransform(repositoryApi: RepositoryApi, sharedPref: SharedPreferencesSource)
            : MeasureUnitsTransformUseCase = MeasureUnitsTransformUseCase(repositoryApi, sharedPref)

}