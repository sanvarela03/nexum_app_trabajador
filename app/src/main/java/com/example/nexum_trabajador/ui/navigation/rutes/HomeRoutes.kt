package com.example.nexum_trabajador.ui.navigation.rutes

import kotlinx.serialization.Serializable


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/4/2025
 * @version 1.0
 */
sealed class HomeRoutes {
    @Serializable
    object RequestsScreen : HomeRoutes()

    @Serializable
    object CategoriesScreen : HomeRoutes()

    @Serializable
    object WalletScreen : HomeRoutes()


    @Serializable
    object AppointmentsScreen : HomeRoutes()


    @Serializable
    object NotificationsScreen : HomeRoutes()

    @Serializable
    object ConversationsScreen : HomeRoutes()

    @Serializable
    data class ChatScreen(
        val conversationId: String,
        val receiverId: String,
        val receiverRole: String,
        val currentUserId: String
    ) : HomeRoutes()

    @Serializable
    data class JobOfferDetailsScreen(val uuid: String) : HomeRoutes()

    @Serializable
    data class JobApplicationGraph(val jobOfferId: Long, val jobOfferUuid: String, val jobOfferTitle: String = "") : HomeRoutes() {
        @Serializable
        object ProposalScreen
        @Serializable
        object TasksScreen
        @Serializable
        object MaterialsScreen
        @Serializable
        object ScheduleScreen
        @Serializable
        object SummaryScreen
    }
}