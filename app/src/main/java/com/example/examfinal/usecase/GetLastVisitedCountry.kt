package com.example.examfinal.usecase

import com.example.examfinal.data.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLastVisitedCountry @Inject constructor(
    private val repository: CountryRepository
) {
    operator fun invoke(): Flow<String?> {
        return repository.getLastVisitedCountry()
    }
}