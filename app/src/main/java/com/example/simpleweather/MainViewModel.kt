package com.example.simpleweather

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.simpleweather.repository.RepositoryApi

class MainViewModel @ViewModelInject constructor(
    private val repository: RepositoryApi,
) : ViewModel() {



}