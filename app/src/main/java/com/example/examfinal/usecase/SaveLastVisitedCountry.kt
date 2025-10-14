package com.example.examfinal.usecase

import com.example.examfinal.data.repository.CountryRepository
import javax.inject.Inject

class SaveLastVisitedCountry @Inject constructor(
    private val repository: CountryRepository
) {
    suspend operator fun invoke(countryName: String) {
        repository.saveLastVisitedCountry(countryName)
    }
}