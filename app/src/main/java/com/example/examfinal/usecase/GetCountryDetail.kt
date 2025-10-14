package com.example.examfinal.usecase

import com.example.examfinal.data.repository.CountryRepository
import com.example.examfinal.domain.CountryDetailDomain
import javax.inject.Inject


/**
 * Caso de uso para obtener el detalle de un país específico.
 *
 * Encapsula la lógica de negocio para la obtención de información
 * detallada de un país individual.
 *
 * @property repository Repositorio de países
 */

class GetCountryDetail @Inject constructor(
    private val repository: CountryRepository
) {
    suspend operator fun invoke(name: String): Result<CountryDetailDomain> {
        return repository.getCountryDetail(name)
    }
}