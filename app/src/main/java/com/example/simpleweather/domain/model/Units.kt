package com.example.simpleweather.domain.model

sealed class Units

    sealed class PressureUnits: Units() {
        companion object { const val key = "units_pressure" }
        object MillimetersOfMercury : PressureUnits() { const val key = "units_millimeter" }
        object HectoPascals : PressureUnits() { const val key = "units_hPa" }
    }

    sealed class DegreeUnits: Units() {
        companion object { const val key = "units_degree" }
        object Celsius : DegreeUnits() { const val key = "units_celsius" }
        object Fahrenheit : DegreeUnits() { const val key = "units_fahrenheit" }
    }

    sealed class WindSpeedUnits: Units() {
        companion object { const val key = "units_wind_speed" }
        object MetersPerSecond : WindSpeedUnits() { const val key = "units_meters_per_second" }
        object KilometersPerHour : WindSpeedUnits() { const val key = "units_kilometers_per_hour" }
    }




