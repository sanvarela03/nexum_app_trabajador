package com.example.nexum_trabajador.data.profile.remote

import com.example.nexum_trabajador.common.apiRequestFlow
import javax.inject.Inject
import javax.inject.Singleton


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 3/12/2026
 * @version 1.0
 */
@Singleton
class ProfileRemoteDataSource @Inject constructor(
    private val api: ProfileApi
) {
    fun getProfile(ids: List<Long>) = apiRequestFlow {
        api.getProfile(ids)
    }
}