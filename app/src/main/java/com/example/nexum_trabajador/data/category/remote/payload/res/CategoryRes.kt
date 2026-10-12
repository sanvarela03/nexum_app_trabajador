package com.example.nexum_trabajador.data.category.remote.payload.res

data class CategoryRes(
    val id: Long,
    val name: String?,
    val description: String?,
    val imageUrl: String?,
    val serviceType: String?,
    val iconName: String? = null
)
