package com.example.nexum_trabajador.data.job_offer.remote.payload.res

data class JobOfferWorkerDetailsDto(
    val id: Long,
    val uuid: String,
    val title: String,
    val description: String,
    val categoryName: String,
    val serviceType: String,
    val iconName: String,
    val photosUrl: List<String>,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val requiredDate: String,
    val clientName: String,
    val clientImageUrl: String?,
    val isClientRecurring: Boolean,
    val distanceInMeters: Double,
    val estimatedDrivingTimeMinutes: Int
)
