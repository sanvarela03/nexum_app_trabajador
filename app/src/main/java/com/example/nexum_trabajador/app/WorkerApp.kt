package com.example.nexum_trabajador.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nexum_trabajador.common.UserAuthState
import com.example.nexum_trabajador.common.protectedComposable
import com.example.nexum_trabajador.ui.navigation.graphs.Graph
import com.example.nexum_trabajador.ui.navigation.graphs.authGraph
import com.example.nexum_trabajador.ui.navigation.rutes.AuthRoutes
import com.example.nexum_trabajador.ui.presenter.home.HomeScreen
import com.example.nexum_trabajador.ui.presenter.sign_up.SignUpViewModel
import com.example.nexum_trabajador.ui.presenter.splash.SplashScreen
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter

import android.content.Intent
import com.example.nexum_trabajador.ui.navigation.rutes.HomeRoutes

@Composable
fun WorkerApp(
    userAuthState: UserAuthState,
    initialIntent: Intent? = null,
    signUpViewModel: SignUpViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val isAuthenticated = userAuthState == UserAuthState.AUTHENTICATED


    LaunchedEffect(userAuthState) {
        snapshotFlow { userAuthState }
            .drop(1)
            .filter { it == UserAuthState.UNAUTHENTICATED }
            .collect {
                navController.navigate(Graph.InitialGraph) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }
    }

    NavHost(
        navController = navController,
        startDestination = getStartDestination(userAuthState)
    ) {
        authGraph(navController, signUpViewModel)
        protectedComposable<AuthRoutes.HomeScreen>(
            isAuthenticated = isAuthenticated,
            onUnauthorized = {
                navController.navigate(Graph.InitialGraph) {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            }) {
            HomeScreen(initialIntent = initialIntent)
        }
        composable<AuthRoutes.SplashScreen>() { SplashScreen(navController = navController) }
    }
}

@Composable
private fun getStartDestination(userAuthState: UserAuthState): Any = when (userAuthState) {
    UserAuthState.UNKNOWN -> {
        AuthRoutes.SplashScreen
    }

    UserAuthState.UNAUTHENTICATED -> {
        Graph.InitialGraph
    }

    UserAuthState.AUTHENTICATED -> {
        AuthRoutes.HomeScreen
    }
}
