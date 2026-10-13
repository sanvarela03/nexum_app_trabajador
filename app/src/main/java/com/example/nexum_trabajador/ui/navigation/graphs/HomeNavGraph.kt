package com.example.nexum_trabajador.ui.navigation.graphs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.nexum_trabajador.ui.navigation.rutes.DrawerRoutes
import com.example.nexum_trabajador.ui.navigation.rutes.HomeRoutes
import com.example.nexum_trabajador.ui.presenter.appointments.AppointmentsScreen
import com.example.nexum_trabajador.ui.presenter.chat.ChatScreen
import com.example.nexum_trabajador.ui.presenter.conversations.ConversationsScreen
import com.example.nexum_trabajador.ui.presenter.home.HomeViewModel
import com.example.nexum_trabajador.ui.presenter.landing.LandingScreen
import com.example.nexum_trabajador.ui.presenter.notifications.NotificationsScreen
import com.example.nexum_trabajador.ui.presenter.occupations.OccupationsScreen
import com.example.nexum_trabajador.ui.presenter.profile.ProfileScreen
import com.example.nexum_trabajador.ui.presenter.work_location.WorkLocationScreen
import androidx.navigation.toRoute
import com.example.nexum_trabajador.ui.presenter.job_offer_details.JobOfferDetailsScreen

//import com.example.nexum_trabajador.ui.navigation.rutes.JobOfferRoutes
//import com.example.nexum_trabajador.ui.presenter.categories.CategoriesScreen
//import com.example.nexum_trabajador.ui.presenter.job_offer.JobOfferScreen
//import com.example.nexum_trabajador.ui.presenter.notifications.NotificationsScreen
//import com.example.nexum_trabajador.ui.presenter.profile.ProfileScreen
//import com.example.nexum_trabajador.ui.presenter.requests.RequestsScreen
//import com.example.nexum_trabajador.ui.presenter.settings.SettingsScreen
//import com.example.nexum_trabajador.ui.presenter.wallet.WalletScreen

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/4/2025
 * @version 1.0
 */
@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.homeGraph(
    navController: NavController,
    homeViewModel: HomeViewModel,
    currentUserId: Long? = null
) {
    navigation<Graph.HomeGraph>(startDestination = HomeRoutes.CategoriesScreen) {
        composable<HomeRoutes.CategoriesScreen> {
            WorkLocationScreen(
                homeViewModel = homeViewModel,
                onBack = null,
                onNavigateToOccupations = {
                    navController.navigate(DrawerRoutes.OccupationsScreen)
                }
            )
        }
        composable<HomeRoutes.AppointmentsScreen> {
            AppointmentsScreen()
        }
        composable<HomeRoutes.RequestsScreen> {

        }
        composable<HomeRoutes.WalletScreen> {

        }

        composable<HomeRoutes.ConversationsScreen> {
            ConversationsScreen(
                currentUserId = currentUserId.toString(),
                onConversationClick = { conversationId, otherUserId, otherUserRole ->
                    navController.navigate(
                        HomeRoutes.ChatScreen(
                            conversationId = conversationId,
                            receiverId = otherUserId,
                            receiverRole = otherUserRole,
                            currentUserId = currentUserId.toString()
                        )
                    )
                }
            )
        }
        composable<HomeRoutes.ChatScreen> { backStackEntry ->
            val args = backStackEntry.toRoute<HomeRoutes.ChatScreen>()
            ChatScreen(
                conversationId = args.conversationId,
                receiverId = args.receiverId,
                receiverRole = args.receiverRole,
                currentUserId = args.currentUserId,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable<HomeRoutes.NotificationsScreen> {
            NotificationsScreen(
                onNavigateToDetails = { jobOfferUuid ->
                    navController.navigate(HomeRoutes.JobOfferDetailsScreen(uuid = jobOfferUuid))
                }
            )
        }
        composable<DrawerRoutes.ProfileScreen> {
            ProfileScreen(
                homeViewModel = homeViewModel,
                onNavigateToOccupations = {
                    navController.navigate(DrawerRoutes.OccupationsScreen)
                },
                onNavigateToInsurances = {
                    navController.navigate(DrawerRoutes.InsurancesScreen)
                }
            )
        }

        composable<DrawerRoutes.OccupationsScreen> {
            OccupationsScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<DrawerRoutes.InsurancesScreen> {
            com.example.nexum_trabajador.ui.presenter.insurance.InsurancesScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<DrawerRoutes.SettingsScreen> {

        }
        composable<DrawerRoutes.AddressScreen> {
            WorkLocationScreen(
                homeViewModel = homeViewModel,
                onBack = {
                    navController.popBackStack()
                },
                onNavigateToOccupations = {
                    navController.navigate(DrawerRoutes.OccupationsScreen)
                }
            )
        }
        composable<HomeRoutes.JobOfferDetailsScreen> { backStackEntry ->
            val args = backStackEntry.toRoute<HomeRoutes.JobOfferDetailsScreen>()
            JobOfferDetailsScreen(
                uuid = args.uuid,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToApplication = { jobOfferId, jobOfferUuid, jobOfferTitle ->
                    navController.navigate(
                        HomeRoutes.JobApplicationGraph(
                            jobOfferId = jobOfferId,
                            jobOfferUuid = jobOfferUuid,
                            jobOfferTitle = jobOfferTitle
                        )
                    )
                }
            )
        }
        
        jobApplicationGraph(navController)
    }

//        composable<JobOfferRoutes.JobOfferScreen> {
//            val args = it.toRoute<JobOfferRoutes.JobOfferScreen>()
//            JobOfferScreen(categoryId = args.categoryId)
//        }
}