package com.example.nexum_trabajador.ui.navigation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.nexum_trabajador.ui.navigation.rutes.DrawerRoutes
import com.example.nexum_trabajador.ui.navigation.rutes.HomeRoutes
import com.example.nexum_trabajador.ui.presenter.home.HomeViewModel
import com.example.nexum_trabajador.ui.presenter.landing.LandingScreen
import com.example.nexum_trabajador.ui.presenter.notifications.NotificationsScreen
import com.example.nexum_trabajador.ui.presenter.profile.ProfileScreen
import com.example.nexum_trabajador.ui.presenter.work_location.WorkLocationScreen
import androidx.navigation.toRoute

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
fun NavGraphBuilder.homeGraph(
    navController: NavController,
    homeViewModel: HomeViewModel
) {
    navigation<Graph.HomeGraph>(startDestination = HomeRoutes.CategoriesScreen) {
        composable<HomeRoutes.CategoriesScreen> {
            LandingScreen(
                homeViewModel = homeViewModel
            )
        }
        composable<HomeRoutes.RequestsScreen> {

        }
        composable<HomeRoutes.WalletScreen> {

        }

        composable<HomeRoutes.NotificationsScreen> {
            NotificationsScreen(
                onNavigateToDetails = { jobOfferUuid ->
                    navController.navigate(HomeRoutes.JobOfferDetailsScreen(uuid = jobOfferUuid))
                }
            )
        }
        composable<DrawerRoutes.ProfileScreen> {
            ProfileScreen(homeViewModel = homeViewModel)
        }

        composable<DrawerRoutes.SettingsScreen> {

        }
        composable<DrawerRoutes.AddressScreen> {
            WorkLocationScreen(
                homeViewModel = homeViewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable<HomeRoutes.JobOfferDetailsScreen> { backStackEntry ->
            val args = backStackEntry.toRoute<HomeRoutes.JobOfferDetailsScreen>()
            com.example.nexum_trabajador.ui.presenter.job_offer_details.JobOfferDetailsScreen(
                uuid = args.uuid,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToApplication = { jobOfferId, jobOfferUuid ->
                    navController.navigate(HomeRoutes.JobApplicationGraph(jobOfferId = jobOfferId, jobOfferUuid = jobOfferUuid))
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