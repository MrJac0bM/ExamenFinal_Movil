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


/**
 * ViewModel para la gestión de países.
 *
 * Maneja la lógica de presentación para la lista de países y los detalles individuales,
 * incluyendo búsqueda, filtrado y persistencia de preferencias del usuario.
 *
 * @property getCountries Caso de uso para obtener la lista de países
 * @property getCountryDetail Caso de uso para obtener el detalle de un país específico
 * @property saveLastVisitedCountry Caso de uso para guardar el último país visitado
 * @property getLastVisitedCountry Caso de uso para recuperar el último país visitado
 */

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

    /**
     * Obtiene la lista completa de países desde la API.
     *
     * Actualiza el estado a Loading durante la carga, y luego a Success o Error
     * dependiendo del resultado. Si existe un último país visitado, prepara el
     * scroll automático hacia ese país.
     */
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

    /**
     * Obtiene el detalle de un país específico por su nombre.
     *
     * @param name Nombre común del país a buscar
     *
     * Actualiza el estado a Loading durante la carga. Si la carga es exitosa,
     * guarda automáticamente el país como último visitado para futuras sesiones.
     */

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


    /**
     * Limpia el estado del detalle del país.
     *
     * Útil al salir de la pantalla de detalle para evitar mostrar
     * datos obsoletos al volver a entrar.
     */
    fun clearDetail() {
        _countryDetailState.value = UiState.Idle
    }

    /**
     * Actualiza la query de búsqueda y filtra la lista de países.
     *
     * @param query Texto de búsqueda ingresado por el usuario
     *
     * Filtra los países por nombre común u oficial, sin distinguir mayúsculas.
     */
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        filterCountries(query)
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _countries.value = _allCountries.value
    }

    /**
     * Filtra la lista de países según el texto de búsqueda.
     *
     * @param query Texto a buscar en los nombres de países
     *
     * Busca coincidencias en el nombre común y oficial del país,
     * sin distinguir entre mayúsculas y minúsculas.
     */
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

    /**
     * Limpia la bandera de scroll automático.
     *
     * Debe llamarse después de realizar el scroll para evitar
     * scrolls repetidos innecesarios.
     */

    fun clearScrollToCountry() {
        _scrollToCountry.value = null
    }

    fun retryLoadCountries() {
        fetchCountries()
    }

    /**
     * Reintenta cargar el detalle de un país específico.
     *
     * @param name Nombre del país a cargar
     *
     * Útil cuando ocurre un error durante la carga del detalle,
     * permitiendo al usuario reintentar la operación.
     */
    fun retryLoadCountryDetail(name: String) {
        fetchCountryDetail(name)
    }
}