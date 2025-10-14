package com.example.examfinal.data.remote.dto

import com.example.examfinal.domain.CountryDetailDomain


data class CountryDetailResponse(
    val name: NameResponse,
    val capital: List<String>? = null,
    val population: Long? = null,
    val region: String? = null,
    val subregion: String? = null,
    val languages: Map<String, String>? = null,
    val flags: FlagsResponse? = null,
    val timezones: List<String>? = null,
    val area: Double? = null
)

data class FlagsResponse(
    val png: String? = null,
    val svg: String? = null,
    val alt: String? = null
)



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