package com.example.simpleweather.utils.datawrappers

sealed class State {
    class Default : State()
    class Loading : State()
    class Error : State()
    class Success : State()
}