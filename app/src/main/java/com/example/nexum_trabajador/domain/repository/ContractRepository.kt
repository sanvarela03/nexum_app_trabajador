package com.example.nexum_trabajador.domain.repository

import com.example.nexum_trabajador.data.contract.remote.payload.req.GenerateInvoiceReq
import com.example.nexum_trabajador.data.contract.remote.payload.res.ContractRes
import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import kotlinx.coroutines.flow.Flow

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @version 1.0
 */
interface ContractRepository {
    fun getMyContracts(): Flow<ApiResponse<List<ContractRes>>>
    fun getContractById(id: Long): Flow<ApiResponse<ContractRes>>
    fun verifyMeeting(id: Long, code: String): Flow<ApiResponse<ContractRes>>
    fun generateInvoice(id: Long, req: GenerateInvoiceReq): Flow<ApiResponse<ContractRes>>
    fun completeByWorker(id: Long): Flow<ApiResponse<ContractRes>>
    fun scheduleMeeting(id: Long, date: String, h3Index: String): Flow<ApiResponse<ContractRes>>
}
