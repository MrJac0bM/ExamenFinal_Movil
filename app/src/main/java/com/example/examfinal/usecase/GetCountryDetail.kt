package com.example.examfinal.usecase

import com.example.examfinal.data.repository.CountryRepository
import com.example.examfinal.domain.CountryDetailDomain
import javax.inject.Inject


class GetCountryDetail @Inject constructor(
    private val repository: CountryRepository
) {
    suspend operator fun invoke(name: String): Result<CountryDetailDomain> {
        return repository.getCountryDetail(name)
    }
}