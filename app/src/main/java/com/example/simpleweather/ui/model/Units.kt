package com.example.simpleweather.ui.model

sealed class Units

    sealed class PressureUnits: Units() {
        object MillimetersOfMercury : PressureUnits()
        object HectoPascals : PressureUnits()
    }

    sealed class DegreeUnits: Units() {
        object Celsius : DegreeUnits()
        object Fahrenheit : DegreeUnits()
    }

    sealed class WindSpeedUnits: Units() {
        object MetersPerSecond : WindSpeedUnits()
        object KilometersPerHour : WindSpeedUnits()
    }




