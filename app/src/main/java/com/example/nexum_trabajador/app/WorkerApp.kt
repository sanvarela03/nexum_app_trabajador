package com.example.nexum_trabajador.app

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WorkerApp(
    userAuthState: UserAuthState,
    startDestination: AuthRoutes,
    initialIntent: Intent? = null,
    signUpViewModel: SignUpViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val isAuthenticated = userAuthState == UserAuthState.AUTHENTICATED

    NavHost(
        navController = navController,
        startDestination = when (userAuthState) {
            UserAuthState.UNKNOWN -> AuthRoutes.SplashScreen
            UserAuthState.UNAUTHENTICATED -> Graph.InitialGraph
            UserAuthState.AUTHENTICATED -> AuthRoutes.HomeScreen
        },
        modifier = Modifier.fillMaxSize()
    ) {
        authGraph(navController, signUpViewModel, startDestination)
        protectedComposable<AuthRoutes.HomeScreen>(
            isAuthenticated = isAuthenticated,
            onUnauthorized = {
                navController.navigate(Graph.InitialGraph) {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            }
        ) {
            HomeScreen(initialIntent = initialIntent)
        }
        composable<AuthRoutes.SplashScreen> {
            SplashScreen(navController = navController)
        }
    }
}
