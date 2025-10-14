package com.example.examfinal.data.remote.dto

import com.example.examfinal.domain.CountryDomain
import com.google.gson.annotations.SerializedName

data class CountryResponse(
    val name: NameResponse
)

data class NameResponse(
    val common: String,
    val official: String,
    val nativeName: Map<String, NativeNameResponse>? = null
)

data class NativeNameResponse(
    val official: String,
    val common: String
)

fun CountryResponse.toDomain(): CountryDomain {
    return CountryDomain(
        commonName = name.common,
        officialName = name.official,
        nativeName = name.nativeName?.values?.firstOrNull()?.common
    )
}


fun List<CountryResponse>.toDomain(): List<CountryDomain> {
    return this.map { it.toDomain() }
}