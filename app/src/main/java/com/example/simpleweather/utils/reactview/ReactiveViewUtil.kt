package com.example.simpleweather.utils.reactview

import androidx.appcompat.widget.SearchView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ReactiveViewUtil {
    companion object {


        @ExperimentalCoroutinesApi
        fun SearchView.searchWatcherFlow(): Flow<String> = callbackFlow {
            send("")
            val listener = object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    sendBlocking(newText)
                    return false
                }
            }
            setOnQueryTextListener(listener)
            awaitClose()
        }








    }
}

