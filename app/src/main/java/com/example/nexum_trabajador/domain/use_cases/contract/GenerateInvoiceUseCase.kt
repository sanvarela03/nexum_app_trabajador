package com.example.nexum_trabajador.domain.use_cases.contract

import com.example.nexum_trabajador.data.contract.remote.payload.req.GenerateInvoiceReq
import com.example.nexum_trabajador.data.contract.remote.payload.res.ContractRes
import com.example.nexum_trabajador.data.global_payload.res.ApiResponse
import com.example.nexum_trabajador.domain.repository.ContractRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @version 1.0
 */
class GenerateInvoiceUseCase @Inject constructor(
    private val repository: ContractRepository
) {
    operator fun invoke(id: Long, finalAmount: Double?, notes: String?): Flow<ApiResponse<ContractRes>> =
        repository.generateInvoice(id, GenerateInvoiceReq(finalAmount = finalAmount, notes = notes))
}
