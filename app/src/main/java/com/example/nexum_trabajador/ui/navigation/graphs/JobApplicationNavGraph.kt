package com.example.nexum_trabajador.ui.navigation.graphs

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.nexum_trabajador.ui.navigation.rutes.HomeRoutes
import com.example.nexum_trabajador.ui.presenter.job_application.JobApplicationViewModel

fun NavGraphBuilder.jobApplicationGraph(
    navController: NavController
) {
    navigation<HomeRoutes.JobApplicationGraph>(startDestination = HomeRoutes.JobApplicationGraph.ProposalScreen) {
        
        composable<HomeRoutes.JobApplicationGraph.ProposalScreen> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(HomeRoutes.JobApplicationGraph::class)
            }
            val viewModel: JobApplicationViewModel = hiltViewModel(parentEntry)
            val args = parentEntry.toRoute<HomeRoutes.JobApplicationGraph>()
            viewModel.init(args.jobOfferId, args.jobOfferUuid)
            
            com.example.nexum_trabajador.ui.presenter.job_application.screens.JobApplicationProposalScreen(
                viewModel = viewModel,
                onNavigateNext = { navController.navigate(HomeRoutes.JobApplicationGraph.TasksScreen) },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<HomeRoutes.JobApplicationGraph.TasksScreen> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(HomeRoutes.JobApplicationGraph::class)
            }
            val viewModel: JobApplicationViewModel = hiltViewModel(parentEntry)
            
            com.example.nexum_trabajador.ui.presenter.job_application.screens.JobApplicationTasksScreen(
                viewModel = viewModel,
                onNavigateNext = { navController.navigate(HomeRoutes.JobApplicationGraph.MaterialsScreen) },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<HomeRoutes.JobApplicationGraph.MaterialsScreen> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(HomeRoutes.JobApplicationGraph::class)
            }
            val viewModel: JobApplicationViewModel = hiltViewModel(parentEntry)
            
            com.example.nexum_trabajador.ui.presenter.job_application.screens.JobApplicationMaterialsScreen(
                viewModel = viewModel,
                onNavigateNext = { navController.navigate(HomeRoutes.JobApplicationGraph.ScheduleScreen) },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<HomeRoutes.JobApplicationGraph.ScheduleScreen> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(HomeRoutes.JobApplicationGraph::class)
            }
            val viewModel: JobApplicationViewModel = hiltViewModel(parentEntry)
            
            com.example.nexum_trabajador.ui.presenter.job_application.screens.JobApplicationScheduleScreen(
                viewModel = viewModel,
                onNavigateNext = { navController.navigate(HomeRoutes.JobApplicationGraph.SummaryScreen) },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<HomeRoutes.JobApplicationGraph.SummaryScreen> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(HomeRoutes.JobApplicationGraph::class)
            }
            val viewModel: JobApplicationViewModel = hiltViewModel(parentEntry)
            
            com.example.nexum_trabajador.ui.presenter.job_application.screens.JobApplicationSummaryScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onSuccess = {
                    navController.popBackStack(HomeRoutes.CategoriesScreen, inclusive = false)
                }
            )
        }
    }
}
