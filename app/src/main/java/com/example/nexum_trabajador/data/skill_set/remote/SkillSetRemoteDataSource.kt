package com.example.nexum_trabajador.data.skill_set.remote

import com.example.nexum_trabajador.common.apiRequestFlow
import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.data.skill_set.remote.payload.req.AddSkillsReq
import com.example.nexum_trabajador.data.skill_set.remote.payload.res.SkillSetRes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SkillSetRemoteDataSource @Inject constructor(
    private val api: SkillSetApi
) {
    fun getAllSkills(): Flow<ApiResponse<List<SkillSetRes>>> =
        apiRequestFlow { api.getAllSkills() }

    fun getSkillsByOccupation(occupationId: Long): Flow<ApiResponse<List<SkillSetRes>>> =
        apiRequestFlow { api.getSkillsByOccupation(occupationId) }

    fun addSkillsToOccupation(occupationId: Long, req: AddSkillsReq): Flow<ApiResponse<List<SkillSetRes>>> =
        apiRequestFlow { api.addSkillsToOccupation(occupationId, req) }

    fun removeSkillFromOccupation(occupationId: Long, skillId: Long): Flow<ApiResponse<List<SkillSetRes>>> =
        apiRequestFlow { api.removeSkillFromOccupation(occupationId, skillId) }
}
