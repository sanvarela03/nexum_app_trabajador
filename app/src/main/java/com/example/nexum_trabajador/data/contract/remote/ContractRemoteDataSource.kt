package com.example.nexum_trabajador.data.contract.remote

import com.example.nexum_trabajador.common.apiRequestFlow
import com.example.nexum_trabajador.data.contract.remote.payload.req.GenerateInvoiceReq
import com.example.nexum_trabajador.data.contract.remote.payload.res.ContractRes
import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @version 1.0
 */
@Singleton
class ContractRemoteDataSource @Inject constructor(
    private val api: ContractApi
) {
    fun getMyContracts(): Flow<ApiResponse<List<ContractRes>>> =
        apiRequestFlow { api.getMyContracts() }

    fun getContractById(id: Long): Flow<ApiResponse<ContractRes>> =
        apiRequestFlow { api.getContractById(id) }

    fun verifyMeeting(id: Long, code: String): Flow<ApiResponse<ContractRes>> =
        apiRequestFlow { api.verifyMeeting(id, code) }

    fun generateInvoice(id: Long, req: GenerateInvoiceReq): Flow<ApiResponse<ContractRes>> =
        apiRequestFlow { api.generateInvoice(id, req) }

    fun completeByWorker(id: Long): Flow<ApiResponse<ContractRes>> =
        apiRequestFlow { api.completeByWorker(id) }

    fun scheduleMeeting(id: Long, date: String, h3Index: String): Flow<ApiResponse<ContractRes>> =
        apiRequestFlow { api.scheduleMeeting(id, date, h3Index) }
}
