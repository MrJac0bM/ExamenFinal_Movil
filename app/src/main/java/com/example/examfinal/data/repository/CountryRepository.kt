package com.example.examfinal.data.repository

import com.example.examfinal.data.local.PreferencesManager

import com.example.examfinal.data.remote.api.CountryApiService
import com.example.examfinal.data.remote.api.toDetailDomain
import com.example.examfinal.data.remote.dto.toDomain
import com.example.examfinal.domain.CountryDetailDomain
import com.example.examfinal.domain.CountryDomain
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


/**
 * Repositorio para la gestión de datos de países.
 *
 * Actúa como única fuente de verdad para los datos de países,
 * coordinando entre la API remota y el almacenamiento local de preferencias.
 *
 * @property apiService Servicio para las llamadas a la API REST Countries
 * @property preferencesManager Gestor de preferencias locales del usuario
 */



class CountryRepository @Inject constructor(
    private val apiService: CountryApiService,
    private val preferencesManager: PreferencesManager
) {

    /**
     * Obtiene la lista completa de países desde la API.
     *
     * @return Result<List<CountryDomain>> Lista de países o error
     *
     * Maneja excepciones de red y convierte los datos de respuesta
     * al modelo de dominio de la aplicación.
     */

    suspend fun getCountries(): Result<List<CountryDomain>> {
        return try {
            val response = apiService.getAllCountries()
            if (response.isEmpty()) {
                Result.failure(Exception("No se encontraron países"))
            } else {
                Result.success(response.map { it.toDomain() })
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error al cargar países: ${e.message}"))
        }
    }
    /**
     * Obtiene el detalle de un país específico por su nombre.
     *
     * @param name Nombre del país a buscar
     * @return Result<CountryDetailDomain> Detalle del país o error
     *
     * Realiza una búsqueda por nombre en la API y retorna el primer resultado.
     * Convierte la respuesta al modelo de dominio de la aplicación.
     */

    suspend fun getCountryDetail(name: String): Result<CountryDetailDomain> {
        return try {
            val response = apiService.getCountryByName(name)
            val country = response.firstOrNull()
            if (country != null) {
                Result.success(country.toDetailDomain())
            } else {
                Result.failure(Exception("País no encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error al cargar el país: ${e.message}"))
        }
    }

    /**
     * Guarda el nombre del último país visitado por el usuario.
     *
     * @param countryName Nombre del país a guardar
     *
     * Almacena la posicion  localmente usando DataStore
     */

    suspend fun saveLastVisitedCountry(countryName: String) {
        preferencesManager.saveLastVisitedCountry(countryName)
    }

    /**
     * Recupera el nombre del último país visitado.
     *
     * @return Flow<String?> Flow que emite el nombre del último país visitado o null
     *
     * Retorna un Flow reactivo que permite observar cambios en la preferencia
     * en tiempo real.
     */
    fun getLastVisitedCountry(): Flow<String?> {
        return preferencesManager.lastVisitedCountry
    }
}