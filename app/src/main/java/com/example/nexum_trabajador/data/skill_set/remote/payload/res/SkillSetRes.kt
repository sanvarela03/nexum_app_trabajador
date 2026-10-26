package com.example.nexum_trabajador.data.skill_set.remote.payload.res

import com.google.gson.annotations.SerializedName

data class SkillSetRes(
    @SerializedName("id")
    val id: Long,

    @SerializedName("name")
    val name: String
)
