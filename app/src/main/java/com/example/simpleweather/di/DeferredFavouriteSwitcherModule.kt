package com.example.simpleweather.di

import com.example.simpleweather.local.DataApi
import com.example.simpleweather.repository.favswitcher.DeferredFavouriteSwitcher
import com.example.simpleweather.repository.favswitcher.DeferredFavouriteSwitcherImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DeferredFavouriteSwitcherModule {

    @Singleton
    @Provides
    fun provideDeferredFavouriteSwitcher(dataApi: DataApi): DeferredFavouriteSwitcher {
        return DeferredFavouriteSwitcherImpl(dataApi)
    }

}