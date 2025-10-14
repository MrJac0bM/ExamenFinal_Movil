package com.example.examfinal.data.remote.api

import com.example.examfinal.data.remote.dto.CountryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CountryApiService {

    @GET("v3.1/all")
    suspend fun getAllCountries(
        @Query("fields") fields: String = "name"
    ): List<CountryResponse>
}