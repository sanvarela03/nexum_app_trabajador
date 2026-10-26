package com.example.nexum_trabajador.common

import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.data.global_payload.res.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.Response

fun <T> apiRequestFlow(
    timeOut: Long = 60000L,
    call: suspend () -> Response<T>
): Flow<ApiResponse<T & Any>> = flow {
    emit(ApiResponse.Loading)
    withTimeoutOrNull(timeOut) {
        val response = call()
        try {
            if (response.isSuccessful) {
                response.body()?.let { data ->
                    emit(ApiResponse.Success(data))
                }
            } else {
                val error = response.errorBody()
                val errorString = try { error?.string().orEmpty() } catch (e: Exception) { "" }
                val parsedError = try {
                    Gson().fromJson(errorString, ErrorResponse::class.java)
                } catch (e: Exception) {
                    null
                }
                val msg = parsedError?.message?.takeIf { it.isNotBlank() }
                    ?: parsedError?.error?.takeIf { it.isNotBlank() }
                    ?: errorString.takeIf { it.isNotBlank() }
                    ?: "Error en la solicitud (${response.code()})"
                val status = parsedError?.status ?: response.code()

                emit(ApiResponse.Failure(msg, status))
            }
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e.message ?: e.toString(), 400))
        }
    } ?: emit(ApiResponse.Failure("Timeout! Please try again.", 408))
}.flowOn(Dispatchers.IO)
