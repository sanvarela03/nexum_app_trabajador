package com.example.nexum_trabajador.domain.repository

import com.example.nexum_trabajador.data.category.remote.payload.res.CategoryRes
import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getCategories(): Flow<ApiResponse<List<CategoryRes>>>
}
