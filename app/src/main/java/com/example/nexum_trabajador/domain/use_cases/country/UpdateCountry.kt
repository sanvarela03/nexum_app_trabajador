package com.example.nexum_trabajador.domain.use_cases.country

import com.example.nexum_trabajador.domain.repository.CountryRepository
import javax.inject.Inject


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 12/2/2025
 * @version 1.0
 */
class UpdateCountry @Inject constructor(
    private val repository: CountryRepository
) {
    operator fun invoke(fetchFromRemote: Boolean) = repository.update(fetchFromRemote)
}