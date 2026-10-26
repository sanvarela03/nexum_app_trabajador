package com.example.nexum_trabajador.data.category.remote

import com.example.nexum_trabajador.data.category.remote.payload.res.CategoryRes
import retrofit2.Response
import retrofit2.http.GET

interface CategoryApi {
    @GET("/api/v1/categories")
    suspend fun getCategories(): Response<List<CategoryRes>>
}
