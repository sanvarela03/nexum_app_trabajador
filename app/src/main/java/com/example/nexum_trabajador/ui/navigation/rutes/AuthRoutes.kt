package com.example.nexum_trabajador.ui.navigation.rutes

import kotlinx.serialization.Serializable


/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 7/23/2025
 * @version 1.0
 */
@Serializable
sealed class AuthRoutes {
    @Serializable
    data class SignInScreen(var username: String? = null) : AuthRoutes()

    @Serializable
    object SignUpScreen : AuthRoutes()

    @Serializable
    object SplashScreen : AuthRoutes()

    @Serializable
    object HomeScreen : AuthRoutes()

    @Serializable
    object SignUpBirthdayScreen : AuthRoutes()

    @Serializable
    object SignUpCellphoneScreen : AuthRoutes()

    @Serializable
    object SignUpUserDataScreen : AuthRoutes()

    @Serializable
    object SignUpUploadDocumentScreen : AuthRoutes()

    @Serializable
    object SignUpCityScreen : AuthRoutes()

    @Serializable
    object SignUpIDScreen : AuthRoutes()

    @Serializable
    object SignUpPasswordScreen : AuthRoutes()

    @Serializable
    object SignUpProfilePictureScreen : AuthRoutes()

    @Serializable
    object SignUpPolicyScreen : AuthRoutes()

    @Serializable
    object SignUpTosScreen : AuthRoutes()

}
