package com.example.nexum_trabajador.ui.navigation.graphs

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/10/2026
 * @version 1.0
 */

private const val SLIDE_OFFSET = 1000
private const val ANIMATION_DURATION = 1000
object Transitions {
    fun defaultEnterTransition(): EnterTransition =
        slideInHorizontally(
            animationSpec = tween(ANIMATION_DURATION),
            initialOffsetX = { SLIDE_OFFSET })

    fun defaultExitTransition(): ExitTransition =
        slideOutHorizontally(
            animationSpec = tween(ANIMATION_DURATION),
            targetOffsetX = { -SLIDE_OFFSET })

    fun defaultPopEnterTransition(): EnterTransition =
        slideInHorizontally(
            animationSpec = tween(ANIMATION_DURATION),
            initialOffsetX = { -SLIDE_OFFSET })

    fun defaultPopExitTransition(): ExitTransition =
        slideOutHorizontally(
            animationSpec = tween(ANIMATION_DURATION),
            targetOffsetX = { SLIDE_OFFSET })
}
