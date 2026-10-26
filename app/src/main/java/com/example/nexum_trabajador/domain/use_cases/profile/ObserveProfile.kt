package com.example.nexum_trabajador.domain.use_cases.profile

import com.example.nexum_trabajador.domain.repository.ProfileRepository
import javax.inject.Inject


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 3/12/2026
 * @version 1.0
 */
class ObserveProfile @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    operator fun invoke() = profileRepository.observeProfiles()
}