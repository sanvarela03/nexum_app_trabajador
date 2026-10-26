package com.example.nexum_trabajador.data.contract.remote.payload.req

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @version 1.0
 */
data class GenerateInvoiceReq(
    val finalAmount: Double?,
    val notes: String?
)
