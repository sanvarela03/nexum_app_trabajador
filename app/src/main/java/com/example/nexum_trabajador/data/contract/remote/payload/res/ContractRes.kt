package com.example.nexum_trabajador.data.contract.remote.payload.res

import com.google.gson.annotations.SerializedName

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @version 1.0
 */
data class ContractRes(
    @SerializedName("id") val id: Long,
    @SerializedName("jobApplicationId") val jobApplicationId: Long,
    @SerializedName("agreedFormOfPaymentId") val agreedFormOfPaymentId: Long?,
    @SerializedName("agreedFormOfPaymentName") val agreedFormOfPaymentName: String?,
    @SerializedName("agreedAmount") val agreedAmount: Double,
    @SerializedName("currencyCode") val currencyCode: String,
    @SerializedName("currentStatus") val currentStatus: String,
    @SerializedName("statusDescription") val statusDescription: String?,
    @SerializedName("meetingDate") val meetingDate: String?,
    @SerializedName("meetingH3Index") val meetingH3Index: String?,
    @SerializedName("verificationCode") val verificationCode: String?,
    @SerializedName("verificationExpiresAt") val verificationExpiresAt: String?,
    @SerializedName("verifiedAt") val verifiedAt: String?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("updatedAt") val updatedAt: String?,
    @SerializedName("workerId") val workerId: Long? = null,
    @SerializedName("workerUserId") val workerUserId: Long? = null,
    @SerializedName("workerName") val workerName: String? = null,
    @SerializedName("workerImageUrl") val workerImageUrl: String? = null,
    @SerializedName("clientId") val clientId: Long? = null,
    @SerializedName("clientUserId") val clientUserId: Long? = null,
    @SerializedName("clientName") val clientName: String? = null,
    @SerializedName("clientImageUrl") val clientImageUrl: String? = null,
    @SerializedName("jobOfferId") val jobOfferId: Long? = null,
    @SerializedName("jobOfferTitle") val jobOfferTitle: String? = null
)
