package com.example.examfinal.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.examfinal.domain.CountryDetailDomain
import com.example.examfinal.domain.CountryDomain
import com.example.examfinal.usecase.GetCountries
import com.example.examfinal.usecase.GetCountryDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryViewModel @Inject constructor(
    private val getCountries: GetCountries,
    private val getCountryDetail: GetCountryDetail
) : ViewModel() {

    private val _allCountries = MutableStateFlow<List<CountryDomain>>(emptyList())

    private val _countries = MutableStateFlow<List<CountryDomain>>(emptyList())
    val countries: StateFlow<List<CountryDomain>> = _countries

    private val _countryDetail = MutableStateFlow<CountryDetailDomain?>(null)
    val countryDetail: StateFlow<CountryDetailDomain?> = _countryDetail

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    fun fetchCountries() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = getCountries()
            _allCountries.value = result
            _countries.value = result
            _isLoading.value = false
        }
    }

    fun fetchCountryDetail(name: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = getCountryDetail(name)
            _countryDetail.value = result
            _isLoading.value = false
        }
    }

    fun clearDetail() {
        _countryDetail.value = null
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        filterCountries(query)
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _countries.value = _allCountries.value
    }

    private fun filterCountries(query: String) {
        _countries.value = if (query.isBlank()) {
            _allCountries.value
        } else {
            _allCountries.value.filter { country ->
                country.commonName.contains(query, ignoreCase = true) ||
                        country.officialName.contains(query, ignoreCase = true)
            }
        }
    }
}