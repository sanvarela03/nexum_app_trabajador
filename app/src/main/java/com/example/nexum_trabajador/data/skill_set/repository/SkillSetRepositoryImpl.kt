package com.example.nexum_trabajador.data.skill_set.repository

import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.data.skill_set.remote.SkillSetRemoteDataSource
import com.example.nexum_trabajador.data.skill_set.remote.payload.req.AddSkillsReq
import com.example.nexum_trabajador.data.skill_set.remote.payload.res.SkillSetRes
import com.example.nexum_trabajador.domain.repository.SkillSetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SkillSetRepositoryImpl @Inject constructor(
    private val remoteDataSource: SkillSetRemoteDataSource
) : SkillSetRepository {

    override fun getAllSkills(): Flow<ApiResponse<List<SkillSetRes>>> =
        remoteDataSource.getAllSkills()

    override fun getSkillsByOccupation(occupationId: Long): Flow<ApiResponse<List<SkillSetRes>>> =
        remoteDataSource.getSkillsByOccupation(occupationId)

    override fun addSkillsToOccupation(occupationId: Long, req: AddSkillsReq): Flow<ApiResponse<List<SkillSetRes>>> =
        remoteDataSource.addSkillsToOccupation(occupationId, req)

    override fun removeSkillFromOccupation(occupationId: Long, skillId: Long): Flow<ApiResponse<List<SkillSetRes>>> =
        remoteDataSource.removeSkillFromOccupation(occupationId, skillId)
}
