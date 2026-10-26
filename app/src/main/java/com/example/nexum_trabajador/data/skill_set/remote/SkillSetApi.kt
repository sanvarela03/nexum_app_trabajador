package com.example.nexum_trabajador.data.skill_set.remote

import com.example.nexum_trabajador.data.skill_set.remote.payload.req.AddSkillsReq
import com.example.nexum_trabajador.data.skill_set.remote.payload.res.SkillSetRes
import retrofit2.Response
import retrofit2.http.*

interface SkillSetApi {
    @GET("/api/v1/skill-sets")
    suspend fun getAllSkills(): Response<List<SkillSetRes>>

    @GET("/api/v1/job-occupations/{occupationId}/skills")
    suspend fun getSkillsByOccupation(@Path("occupationId") occupationId: Long): Response<List<SkillSetRes>>

    @POST("/api/v1/job-occupations/{occupationId}/skills")
    suspend fun addSkillsToOccupation(
        @Path("occupationId") occupationId: Long,
        @Body req: AddSkillsReq
    ): Response<List<SkillSetRes>>

    @DELETE("/api/v1/job-occupations/{occupationId}/skills/{skillId}")
    suspend fun removeSkillFromOccupation(
        @Path("occupationId") occupationId: Long,
        @Path("skillId") skillId: Long
    ): Response<List<SkillSetRes>>
}
