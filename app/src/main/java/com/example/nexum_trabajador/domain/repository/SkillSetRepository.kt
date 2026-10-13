package com.example.nexum_trabajador.domain.repository

import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.data.skill_set.remote.payload.req.AddSkillsReq
import com.example.nexum_trabajador.data.skill_set.remote.payload.res.SkillSetRes
import kotlinx.coroutines.flow.Flow

interface SkillSetRepository {
    fun getAllSkills(): Flow<ApiResponse<List<SkillSetRes>>>
    fun getSkillsByOccupation(occupationId: Long): Flow<ApiResponse<List<SkillSetRes>>>
    fun addSkillsToOccupation(occupationId: Long, req: AddSkillsReq): Flow<ApiResponse<List<SkillSetRes>>>
    fun removeSkillFromOccupation(occupationId: Long, skillId: Long): Flow<ApiResponse<List<SkillSetRes>>>
}
