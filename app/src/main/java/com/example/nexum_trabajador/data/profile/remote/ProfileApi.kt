package com.example.nexum_trabajador.data.profile.remote

import com.example.nexum_trabajador.data.profile.remote.payload.res.ProfileRes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 3/9/2026
 * @version 1.0
 */
interface ProfileApi {
    @GET("/api/v1/users/profiles")
    suspend fun getProfile(@Query("user-ids") userIds: List<Long>): Response<List<ProfileRes>>
}