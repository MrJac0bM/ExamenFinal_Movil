package com.example.examfinal.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.examfinal.domain.CountryDomain
import com.example.examfinal.usecase.GetCountries
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryViewModel @Inject constructor(
    private val getCountries: GetCountries,
) : ViewModel() {

    private val _countries = MutableStateFlow<List<CountryDomain>>(emptyList())
    val countries: StateFlow<List<CountryDomain>> = _countries.asStateFlow()



    init {
        loadCountries()
    }

    fun loadCountries() {
        viewModelScope.launch {
            val countries = getCountries()
            _countries.value = countries
        }
    }

    fun fetchCountries() {
        viewModelScope.launch {
            val result = getCountries()
            _countries.value = result
        }
    }




}