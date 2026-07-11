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

class UpdateProfile @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(ids: List<Long>, fetchFromRemote: Boolean) =
        profileRepository.updateProfile(ids, fetchFromRemote)
}