package com.example.examfinal.domain

data class CountryDomain(
    val commonName: String,
    val officialName: String,
    val nativeName: String? = null
)