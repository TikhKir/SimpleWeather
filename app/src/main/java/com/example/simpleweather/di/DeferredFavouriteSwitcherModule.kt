package com.example.simpleweather.di

import com.example.simpleweather.local.DataApi
import com.example.simpleweather.utils.favswitcher.DeferredFavouriteSwitcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DeferredFavouriteSwitcherModule {

    @Singleton
    @Provides
    fun provideDeferredFavouriteSwitcher(dataApi: DataApi): DeferredFavouriteSwitcher {
        return DeferredFavouriteSwitcher(dataApi)
    }

}