package com.example.nexum_trabajador.ui.presenter.sign_up.screens

import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nexum_trabajador.R
import com.example.nexum_trabajador.common.capitalizeFirstLetter
import com.example.nexum_trabajador.ui.components.ButtonComponent
import com.example.nexum_trabajador.ui.components.ImagePreview
import com.example.nexum_trabajador.ui.components.SignUpSuccessDialog
import com.example.nexum_trabajador.ui.navigation.rutes.AuthRoutes
import com.example.nexum_trabajador.ui.presenter.sign_up.SignUpEvent
import com.example.nexum_trabajador.ui.presenter.sign_up.SignUpState
import com.example.nexum_trabajador.ui.presenter.sign_up.SignUpViewModel
import com.example.nexum_trabajador.ui.theme.Nexum_trabajadorTheme

/**
 * @author Ernesto Bastidas Pulido
 * @email ebastidasp@unal.edu.co
 * @github https://github.com/ebastidasp
 * @since 20/08/2025
 * @version 1.0
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SignUpProfilePictureScreen(
    viewModel: SignUpViewModel,
    navController: NavController,
    go: (Any) -> Unit = {}
) {
    val state = viewModel.state
    val context = LocalContext.current

    val capturedUsername = remember(state.username) { state.username }

    LaunchedEffect(state.signUpError) {
        if (state.signUpError) {
            Toast.makeText(context, state.errorMessage, Toast.LENGTH_LONG).show()
            viewModel.resetSignUpGlobalValidation()
        }
    }

    SignUpProfilePictureScreenContent(
        state = state,
        username = capturedUsername,
        onProfilePictureChanged = {
            viewModel.onEvent(SignUpEvent.ProfilePictureUriChanged(it))
        },
        onRemoveProfilePicture = {
            viewModel.onEvent(SignUpEvent.ProfilePictureUriChanged(Uri.EMPTY))
        },
        onRegister = {
            viewModel.onEvent(SignUpEvent.RegisterButtonClicked)
        },
        onAcceptAndNavigate = {
            viewModel.onEvent(SignUpEvent.DismissSignUpSuccessDialog)
            navController.navigate(AuthRoutes.SignInScreen(username = capturedUsername)) {
                popUpTo(AuthRoutes.SignUpScreen) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    )
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun SignUpProfilePictureScreenContent(
    state: SignUpState = SignUpState(),
    username: String = "",
    onProfilePictureChanged: (Uri) -> Unit = {},
    onRemoveProfilePicture: (Int) -> Unit = {},
    onRegister: () -> Unit = {},
    onAcceptAndNavigate: () -> Unit = {}
) {
    val insets = WindowInsets.navigationBars.asPaddingValues()
    Nexum_trabajadorTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 20.dp,
                    top = 60.dp,
                    end = 20.dp,
                    bottom = insets.calculateBottomPadding() + 25.dp
                ),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Perfil",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )

                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit profile",
                        tint = Color.Black,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(128.dp)
                        .semantics { contentDescription = "Profile image with camera button" },
                    contentAlignment = Alignment.Center
                ) {
                    if (state.profilePictureUrl.isNotEmpty()) {
                        ImagePreview(
                            contentModifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            containerModifier = Modifier.fillMaxSize(),
                            uri = state.profilePictureUrl,
                            onRemoveImage = onRemoveProfilePicture,
                            index = 0,
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Color(0xFFBDBDBD)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                modifier = Modifier.size(80.dp),
                                contentDescription = "Default avatar",
                                tint = Color.White
                            )
                        }
                    }
                    EditProfilePictureButton(
                        onProfilePictureUriChanged = onProfilePictureChanged
                    )
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 20.dp, end = 20.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(100.dp))
                Row {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email icon",
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = state.email)
                }
                Row {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Phone icon",
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "${state.phoneCode} ${state.phone}")
                }
                Row {
                    Icon(
                        painter = painterResource(R.drawable.vector),
                        contentDescription = "ID icon",
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = state.documentNumber)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = "Location icon",
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(5.dp))
                state.selectedMarketLocation?.let {
                    Text(text = "${it.city.capitalizeFirstLetter()}, ${it.state.capitalizeFirstLetter()}, ${it.country.capitalizeFirstLetter()}")
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            ButtonComponent(
                value = "Terminar registro",
                isLoading = state.isSigningUp,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                    contentColor = Color.White,
                    disabledContentColor = MaterialTheme.colorScheme.secondary
                ),
                isEnabled = state.isProfilePictureValid && !state.isSigningUp
            ) {
                onRegister()
            }

            SignUpSuccessDialog(
                show = state.isSignedUp,
                username = username,
                onAccept = onAcceptAndNavigate
            )
        }
    }
}

@Composable
private fun BoxScope.EditProfilePictureButton(
    onProfilePictureUriChanged: (Uri) -> Unit = {}
) {
    val profilePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                onProfilePictureUriChanged(it)
            }
        }
    )
    Surface(
        shape = CircleShape,
        color = Color.Gray,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .size(32.dp)
            .clickable {
                profilePickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Outlined.AddAPhoto,
                contentDescription = "Change photo",
                tint = Color.Black
            )
        }
    }
}
