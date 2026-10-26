package com.example.nexum_trabajador.domain.repository

import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.domain.model.Profile
import kotlinx.coroutines.flow.Flow

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 3/12/2026
 * @version 1.0
 */
interface ProfileRepository {
    suspend fun updateProfile(
        userIds: List<Long>,
        fetchFromRemote: Boolean
    ): Flow<ApiResponse<Unit>>

    fun observeProfiles(): Flow<List<Profile>>
}