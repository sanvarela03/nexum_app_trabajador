package com.example.nexum_trabajador.ui.navigation.graphs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
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
import com.example.nexum_trabajador.ui.presenter.sign_up.screens.SignUpProfilePictureScreen
import com.example.nexum_trabajador.ui.presenter.sign_up.screens.SignUpScreen
import com.example.nexum_trabajador.ui.presenter.sign_up.screens.SignUpUploadDocumentScreen
import com.example.nexum_trabajador.ui.presenter.sign_up.screens.SignUpUserDataScreen

private const val ANIMATION_DURATION = 1000

private typealias AnimationScope = AnimatedContentTransitionScope<NavBackStackEntry>

data class ScreenTransitions(
    val enterTransition: AnimationScope.() -> EnterTransition? = { Transitions.defaultEnterTransition() },
    val exitTransition: AnimationScope.() -> ExitTransition? = { Transitions.defaultExitTransition() },
    val popEnterTransition: AnimationScope.() -> EnterTransition? = { Transitions.defaultPopEnterTransition() },
    val popExitTransition: AnimationScope.() -> ExitTransition? = { Transitions.defaultPopExitTransition() }
)

inline fun <reified T : Any> NavGraphBuilder.composableWithTransitions(
    transitions: ScreenTransitions = ScreenTransitions(),
    noinline content: @Composable (NavBackStackEntry) -> Unit
) {
    composable<T>(
        enterTransition = transitions.enterTransition,
        exitTransition = transitions.exitTransition,
        popEnterTransition = transitions.popEnterTransition,
        popExitTransition = transitions.popExitTransition,
    ) { backStackEntry ->
        content(backStackEntry)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.authGraph(
    navController: NavController,
    viewModel: SignUpViewModel,
    startDestination: AuthRoutes
) {
    navigation<Graph.InitialGraph>(
        startDestination = startDestination,
    ) {
        val firstScreenTransitions = ScreenTransitions(
            enterTransition = { fadeIn(animationSpec = tween(ANIMATION_DURATION)) },
            exitTransition = { fadeOut(animationSpec = tween(ANIMATION_DURATION)) },
            popEnterTransition = { fadeIn(animationSpec = tween(ANIMATION_DURATION)) },
            popExitTransition = { fadeOut(animationSpec = tween(ANIMATION_DURATION)) }
        )

        val onBack = { navController.popBackStack() }

        composableWithTransitions<AuthRoutes.SignInScreen> {
            val args = it.toRoute<AuthRoutes.SignInScreen>()
            SignInScreen(username = args.username, go = { navController.navigate(it) })
        }

        composableWithTransitions<AuthRoutes.SignUpScreen>(transitions = firstScreenTransitions) { backStackEntry ->
            SignUpScreen(go = { navController.navigate(it) }, viewModel = viewModel)
        }

        composableWithTransitions<AuthRoutes.SignUpUserDataScreen> { backStackEntry ->
            SignUpUserDataScreen(
                go = { navController.navigate(it) },
                onBack = onBack,
                viewModel = viewModel
            )
        }

        composableWithTransitions<AuthRoutes.SignUpCellphoneScreen> { backStackEntry ->
            SignUpCellphoneScreen(
                go = { navController.navigate(it) },
                onBack = onBack,
                viewModel = viewModel
            )
        }

        composableWithTransitions<AuthRoutes.SignUpBirthdayScreen> { backStackEntry ->
            SignUpBirthDateScreen(
                go = { navController.navigate(it) },
                onBack = onBack,
                viewModel = viewModel
            )
        }

        composableWithTransitions<AuthRoutes.SignUpCityScreen> { backStackEntry ->
            SignUpCityScreen(
                go = { navController.navigate(it) },
                onBack = onBack,
                viewModel = viewModel
            )
        }

        composableWithTransitions<AuthRoutes.SignUpIDScreen> { backStackEntry ->
            SignUpIDScreen(
                go = { navController.navigate(it) },
                onBack = onBack,
                viewModel = viewModel
            )
        }

        composableWithTransitions<AuthRoutes.SignUpUploadDocumentScreen> { backStackEntry ->
            SignUpUploadDocumentScreen(
                go = { navController.navigate(it) },
                onBack = onBack,
                viewModel = viewModel
            )
        }

        composableWithTransitions<AuthRoutes.SignUpPasswordScreen> { backStackEntry ->
            SignUpPasswordScreen(
                go = { navController.navigate(it) },
                onBack = onBack,
                viewModel = viewModel
            )
        }

        composableWithTransitions<AuthRoutes.SignUpProfilePictureScreen> { backStackEntry ->
            SignUpProfilePictureScreen(
                navController = navController,
                go = { navController.navigate(it) },
                viewModel = viewModel
            )
        }
    }
}
