package com.example.examfinal.data.remote.api

import com.example.examfinal.data.remote.dto.CountryDetailResponse
import com.example.examfinal.data.remote.dto.CountryResponse
import com.example.examfinal.domain.CountryDetailDomain
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CountryApiService {

    @GET("v3.1/all")
    suspend fun getAllCountries(
        @Query("fields") fields: String = "name"
    ): List<CountryResponse>

    @GET("v3.1/name/{name}")
    suspend fun getCountryByName(
        @Path("name") name: String
    ): List<CountryDetailResponse>
}

fun CountryDetailResponse.toDetailDomain(): CountryDetailDomain {
    return CountryDetailDomain(
        commonName = name.common,
        officialName = name.official,
        capital = capital?.firstOrNull() ?: "N/A",
        population = population?.let { formatNumber(it) } ?: "N/A",
        region = region ?: "N/A",
        subregion = subregion ?: "N/A",
        languages = languages?.values?.joinToString(", ") ?: "N/A",
        flagUrl = flags?.png ?: "",
        timezones = timezones?.joinToString(", ") ?: "N/A",
        area = area?.let { "$it km²" } ?: "N/A"
    )
}

private fun formatNumber(number: Long): String {
    return String.format("%,d", number)
}