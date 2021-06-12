package com.example.simpleweather.utils

sealed class State {
    class Default : State()
    class Loading : State()
    class Error(val errorMessage: String) : State()
    class Success : State()
}