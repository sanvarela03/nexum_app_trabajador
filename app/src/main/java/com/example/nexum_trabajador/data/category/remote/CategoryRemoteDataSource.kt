package com.example.nexum_trabajador.data.category.remote

import com.example.nexum_trabajador.common.apiRequestFlow
import com.example.nexum_trabajador.data.category.remote.payload.res.CategoryRes
import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRemoteDataSource @Inject constructor(
    private val api: CategoryApi
) {
    fun getCategories(): Flow<ApiResponse<List<CategoryRes>>> =
        apiRequestFlow { api.getCategories() }
}
