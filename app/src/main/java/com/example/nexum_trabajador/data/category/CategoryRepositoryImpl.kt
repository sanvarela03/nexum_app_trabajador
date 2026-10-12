package com.example.nexum_trabajador.data.category

import com.example.nexum_trabajador.data.category.remote.CategoryRemoteDataSource
import com.example.nexum_trabajador.data.category.remote.payload.res.CategoryRes
import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val remoteDataSource: CategoryRemoteDataSource
) : CategoryRepository {
    override fun getCategories(): Flow<ApiResponse<List<CategoryRes>>> =
        remoteDataSource.getCategories()
}
