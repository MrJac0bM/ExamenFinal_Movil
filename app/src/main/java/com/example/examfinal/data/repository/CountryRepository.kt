package com.example.examfinal.data.repository

import com.example.examfinal.data.remote.api.CountryApiService
import com.example.examfinal.data.remote.api.toDetailDomain
import com.example.examfinal.data.remote.dto.toDomain
import com.example.examfinal.domain.CountryDetailDomain
import com.example.examfinal.domain.CountryDomain
import javax.inject.Inject


class CountryRepository @Inject constructor(
    private val apiService: CountryApiService
) {

    suspend fun getCountries(): List<CountryDomain> {
        return try {
            val response = apiService.getAllCountries()
            response.map { it.toDomain() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getCountryDetail(name: String): CountryDetailDomain? {
        return try {
            val response = apiService.getCountryByName(name)
            response.firstOrNull()?.toDetailDomain()
        } catch (e: Exception) {
            null
        }
    }
}