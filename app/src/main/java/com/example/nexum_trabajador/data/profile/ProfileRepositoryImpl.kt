package com.example.nexum_trabajador.data.profile

import com.example.nexum_trabajador.common.updateIncrementalResourceFlow
import com.example.nexum_trabajador.common.updateResourceFlow
import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.data.profile.local.ProfileLocalDataSource
import com.example.nexum_trabajador.data.profile.mapper.ProfileMapper
import com.example.nexum_trabajador.data.profile.remote.ProfileRemoteDataSource
import com.example.nexum_trabajador.di.modules.IoDispatcher
import com.example.nexum_trabajador.domain.model.Profile
import com.example.nexum_trabajador.domain.repository.ProfileRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 3/12/2026
 * @version 1.0
 */
@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val remote: ProfileRemoteDataSource,
    private val local: ProfileLocalDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ProfileRepository {
    override suspend fun updateProfile(
        userIds: List<Long>,
        fetchFromRemote: Boolean
    ): Flow<ApiResponse<Unit>> =
        updateIncrementalResourceFlow(
            fetchFromRemote = fetchFromRemote,
            remoteCall = { remote.getProfile(userIds) },
            saveToCache = { local.insertAll(ProfileMapper.toEntity(it)) },
            dispatcher = dispatcher
        )

    override fun observeProfiles(): Flow<List<Profile>> {
        return local.observe().map { list ->
            list.map { ProfileMapper.toDomain(it) }
        }
    }
}