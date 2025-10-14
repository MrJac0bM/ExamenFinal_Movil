package com.example.examfinal.usecase

import com.example.examfinal.data.repository.CountryRepository
import com.example.examfinal.domain.CountryDomain
import javax.inject.Inject

class GetCountries @Inject constructor(
    private val repository: CountryRepository
) {
    suspend operator fun invoke(): List<CountryDomain> {
        return repository.getCountries()
    }
}