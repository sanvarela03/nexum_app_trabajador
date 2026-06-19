package com.example.nexum_trabajador.data.notification.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val title: String,
    val body: String,
    val createdAt: String,
    
    val type: String? = null,
    val jobOfferUuid: String? = null,
    val clientName: String? = null,
    val clientImageUrl: String? = null,
    val jobOfferTitle: String? = null,
    val requiredDate: String? = null,
    val locationName: String? = null,
    val categoryName: String? = null,
    val serviceType: String? = null
)