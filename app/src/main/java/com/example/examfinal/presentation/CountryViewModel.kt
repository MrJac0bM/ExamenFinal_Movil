package com.example.examfinal.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.examfinal.domain.CountryDetailDomain
import com.example.examfinal.domain.CountryDomain
import com.example.examfinal.usecase.GetCountries
import com.example.examfinal.usecase.GetCountryDetail
import com.example.examfinal.usecase.GetLastVisitedCountry
import com.example.examfinal.usecase.SaveLastVisitedCountry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryViewModel @Inject constructor(
    private val getCountries: GetCountries,
    private val getCountryDetail: GetCountryDetail,
    private val saveLastVisitedCountry: SaveLastVisitedCountry,
    private val getLastVisitedCountry: GetLastVisitedCountry
) : ViewModel() {

    private val _allCountries = MutableStateFlow<List<CountryDomain>>(emptyList())

    private val _countriesState = MutableStateFlow<UiState<List<CountryDomain>>>(UiState.Idle)
    val countriesState: StateFlow<UiState<List<CountryDomain>>> = _countriesState

    private val _countries = MutableStateFlow<List<CountryDomain>>(emptyList())
    val countries: StateFlow<List<CountryDomain>> = _countries

    private val _countryDetailState = MutableStateFlow<UiState<CountryDetailDomain>>(UiState.Idle)
    val countryDetailState: StateFlow<UiState<CountryDetailDomain>> = _countryDetailState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _lastVisitedCountry = MutableStateFlow<String?>(null)
    val lastVisitedCountry: StateFlow<String?> = _lastVisitedCountry

    private val _scrollToCountry = MutableStateFlow<String?>(null)
    val scrollToCountry: StateFlow<String?> = _scrollToCountry

    init {
        viewModelScope.launch {
            getLastVisitedCountry().collect { countryName ->
                _lastVisitedCountry.value = countryName
            }
        }
    }

    fun fetchCountries() {
        viewModelScope.launch {
            _countriesState.value = UiState.Loading

            val result = getCountries()

            result.onSuccess { countryList ->
                _allCountries.value = countryList
                _countries.value = countryList
                _countriesState.value = UiState.Success(countryList)


                _lastVisitedCountry.value?.let { lastCountry ->
                    _scrollToCountry.value = lastCountry
                }
            }.onFailure { exception ->
                _countriesState.value = UiState.Error(
                    exception.message ?: "Error  al cargar países"
                )
            }
        }
    }

    fun fetchCountryDetail(name: String) {
        viewModelScope.launch {
            _countryDetailState.value = UiState.Loading

            val result = getCountryDetail(name)

            result.onSuccess { country ->
                _countryDetailState.value = UiState.Success(country)

                saveLastVisitedCountry(name)
            }.onFailure { exception ->
                _countryDetailState.value = UiState.Error(
                    exception.message ?: "Error desconocido al cargar el país"
                )
            }
        }
    }

    fun clearDetail() {
        _countryDetailState.value = UiState.Idle
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

    fun clearScrollToCountry() {
        _scrollToCountry.value = null
    }

    fun retryLoadCountries() {
        fetchCountries()
    }

    fun retryLoadCountryDetail(name: String) {
        fetchCountryDetail(name)
    }
}