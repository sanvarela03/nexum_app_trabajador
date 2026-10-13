package com.example.nexum_trabajador.data.skill_set.remote.payload.req

import com.google.gson.annotations.SerializedName

data class AddSkillsReq(
    @SerializedName("skills")
    val skills: List<String>
)
