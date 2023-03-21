package com.example.simpleweather.data.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class SharedPreferencesSourceImpl @Inject constructor(context: Context): SharedPreferencesSource {

    private val preferencesManager = PreferenceManager.getDefaultSharedPreferences(context)
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    @ExperimentalCoroutinesApi
    private val preferencesFlow = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, _ ->
            val newEmit = sharedPreferences.all
            this.trySend(newEmit).isSuccess
        }
        preferencesManager.registerOnSharedPreferenceChangeListener(listener)
        awaitClose { preferencesManager.unregisterOnSharedPreferenceChangeListener(listener) }
    }.stateIn(
        scope,
        SharingStarted.Lazily,
        preferencesManager.all //first emit
    )

    @ExperimentalCoroutinesApi
    override fun getSharedPreferences(): StateFlow<MutableMap<String, *>> = preferencesFlow
}