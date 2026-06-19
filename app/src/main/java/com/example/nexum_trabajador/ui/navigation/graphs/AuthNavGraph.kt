package com.example.nexum_trabajador.ui.navigation.graphs

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.nexum_trabajador.ui.navigation.rutes.AuthRoutes
import com.example.nexum_trabajador.ui.presenter.sign_in.SignInScreen
import com.example.nexum_trabajador.ui.presenter.sign_up.SignUpViewModel
import com.example.nexum_trabajador.ui.presenter.sign_up.screens.SignUpBirthDateScreen
import com.example.nexum_trabajador.ui.presenter.sign_up.screens.SignUpCellphoneScreen
import com.example.nexum_trabajador.ui.presenter.sign_up.screens.SignUpCityScreen
import com.example.nexum_trabajador.ui.presenter.sign_up.screens.SignUpIDScreen
import com.example.nexum_trabajador.ui.presenter.sign_up.screens.SignUpPasswordScreen
import com.example.nexum_trabajador.ui.presenter.sign_up.screens.SignUpPolicyScreen
import com.example.nexum_trabajador.ui.presenter.sign_up.screens.SignUpProfilePictureScreen
import com.example.nexum_trabajador.ui.presenter.sign_up.screens.SignUpScreen
import com.example.nexum_trabajador.ui.presenter.sign_up.screens.SignUpTosScreen
import com.example.nexum_trabajador.ui.presenter.sign_up.screens.SignUpUploadDocumentScreen
import com.example.nexum_trabajador.ui.presenter.sign_up.screens.SignUpUserDataScreen

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 7/23/2025
 * @version 1.0
 */
fun NavGraphBuilder.authGraph(
    navController: NavController,
    signUpViewModel: SignUpViewModel
) {
    navigation<Graph.InitialGraph>(startDestination = AuthRoutes.SignUpScreen) {
        composable<AuthRoutes.SignInScreen>(

        ) {
            val args = it.toRoute<AuthRoutes.SignInScreen>()
            val username = args.username
            SignInScreen(
                username = username,
                go = { navController.navigate(it) }
            )
        }
        composable<AuthRoutes.SignUpScreen>(
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(1000)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(1000)
                )
            }
        ) { SignUpScreen(go = { navController.navigate(it) }, viewModel = signUpViewModel) }
        composable<AuthRoutes.SignUpUserDataScreen>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(1000)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(1000)
                )
            }
        ) {
            SignUpUserDataScreen(
                go = { navController.navigate(it) },
                viewModel = signUpViewModel
            )
        }
        composable<AuthRoutes.SignUpCellphoneScreen> {
            SignUpCellphoneScreen(
                go = { navController.navigate(it) },
                viewModel = signUpViewModel
            )
        }
        composable<AuthRoutes.SignUpBirthdayScreen> {
            SignUpBirthDateScreen(
                go = { navController.navigate(it) },
                viewModel = signUpViewModel
            )
        }
        composable<AuthRoutes.SignUpCityScreen> {
            SignUpCityScreen(
                go = { navController.navigate(it) },
                viewModel = signUpViewModel
            )
        }
        composable<AuthRoutes.SignUpIDScreen> {
            SignUpIDScreen(
                go = { navController.navigate(it) },
                viewModel = signUpViewModel
            )
        }

        composable<AuthRoutes.SignUpUploadDocumentScreen> {
            SignUpUploadDocumentScreen(
                go = { navController.navigate(it) },
                viewModel = signUpViewModel
            )
        }

        composable<AuthRoutes.SignUpPasswordScreen> {
            SignUpPasswordScreen(
                go = { navController.navigate(it) },
                viewModel = signUpViewModel
            )
        }


        composable<AuthRoutes.SignUpProfilePictureScreen> {
            SignUpProfilePictureScreen(
                go = { navController.navigate(it) },
                viewModel = signUpViewModel
            )
        }

        composable<AuthRoutes.SignUpPolicyScreen> {
            SignUpPolicyScreen()
        }
        composable<AuthRoutes.SignUpTosScreen> {
            SignUpTosScreen()
        }
    }
}
