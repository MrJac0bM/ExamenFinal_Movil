package com.example.examfinal.usecase

import com.example.examfinal.data.repository.CountryRepository
import com.example.examfinal.domain.CountryDetailDomain
import com.example.examfinal.domain.CountryDomain
import javax.inject.Inject


/**
 * Caso de uso para obtener la lista de países.
 *
 * Implementa el principio de responsabilidad única encapsulando
 * la lógica de negocio para obtener países.
 *
 * @property repository Repositorio de países
 */
class GetCountries @Inject constructor(
    private val repository: CountryRepository
) {
    suspend operator fun invoke(): Result<List<CountryDomain>> {
        return repository.getCountries()
    }
}