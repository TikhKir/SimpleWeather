package com.example.simpleweather.utils

import androidx.appcompat.widget.SearchView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.text.DecimalFormat

@ExperimentalCoroutinesApi
fun SearchView.searchWatcherFlow(): Flow<String> = callbackFlow {
    send("")
    val listener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            offer(newText)
            return false
        }
    }
    setOnQueryTextListener(listener)
    awaitClose()
}

fun Float.toUIFormat(): String {
    val decimalFormat = DecimalFormat("#.#")
    return decimalFormat.format(this)
}


