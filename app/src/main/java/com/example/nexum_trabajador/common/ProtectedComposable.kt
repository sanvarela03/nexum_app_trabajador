package com.example.nexum_trabajador.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 2/3/2026
 * @version 1.0
 */
inline fun <reified T : Any> NavGraphBuilder.protectedComposable(
    isAuthenticated: Boolean,
    noinline onUnauthorized: () -> Unit,
    noinline content: @Composable (NavBackStackEntry) -> Unit
) {
    composable<T> { backStackEntry ->
        if (isAuthenticated) {
            content(backStackEntry)
        } else {
            LaunchedEffect(Unit) {
                onUnauthorized()
            }
        }
    }
}
