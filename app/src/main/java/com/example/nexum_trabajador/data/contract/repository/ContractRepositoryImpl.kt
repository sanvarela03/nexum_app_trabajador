package com.example.nexum_trabajador.data.contract.repository

import com.example.nexum_trabajador.data.contract.remote.ContractRemoteDataSource
import com.example.nexum_trabajador.data.contract.remote.payload.req.GenerateInvoiceReq
import com.example.nexum_trabajador.data.contract.remote.payload.res.ContractRes
import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.domain.repository.ContractRepository
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
class ContractRepositoryImpl @Inject constructor(
    private val remoteDataSource: ContractRemoteDataSource
) : ContractRepository {

    override fun getMyContracts(): Flow<ApiResponse<List<ContractRes>>> =
        remoteDataSource.getMyContracts()

    override fun getContractById(id: Long): Flow<ApiResponse<ContractRes>> =
        remoteDataSource.getContractById(id)

    override fun verifyMeeting(id: Long, code: String): Flow<ApiResponse<ContractRes>> =
        remoteDataSource.verifyMeeting(id, code)

    override fun generateInvoice(id: Long, req: GenerateInvoiceReq): Flow<ApiResponse<ContractRes>> =
        remoteDataSource.generateInvoice(id, req)

    override fun completeByWorker(id: Long): Flow<ApiResponse<ContractRes>> =
        remoteDataSource.completeByWorker(id)

    override fun scheduleMeeting(id: Long, date: String, h3Index: String): Flow<ApiResponse<ContractRes>> =
        remoteDataSource.scheduleMeeting(id, date, h3Index)
}
