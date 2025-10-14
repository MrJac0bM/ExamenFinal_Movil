package com.example.examfinal.data.repository

import com.example.examfinal.data.local.PreferencesManager

import com.example.examfinal.data.remote.api.CountryApiService
import com.example.examfinal.data.remote.api.toDetailDomain
import com.example.examfinal.data.remote.dto.toDomain
import com.example.examfinal.domain.CountryDetailDomain
import com.example.examfinal.domain.CountryDomain
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CountryRepository @Inject constructor(
    private val apiService: CountryApiService,
    private val preferencesManager: PreferencesManager
) {

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

    suspend fun saveLastVisitedCountry(countryName: String) {
        preferencesManager.saveLastVisitedCountry(countryName)
    }

    fun getLastVisitedCountry(): Flow<String?> {
        return preferencesManager.lastVisitedCountry
    }
}