package com.example.nexum_trabajador.ui.presenter.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.OnlinePrediction
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.nexum_trabajador.R
import com.example.nexum_trabajador.common.formatToReadableDate
import com.example.nexum_trabajador.common.toLocalDateTime
import com.example.nexum_trabajador.domain.model.Worker
import com.example.nexum_trabajador.ui.presenter.home.HomeViewModel

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 12/30/2025
 * @version 1.0
 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileScreen(
    homeViewModel: HomeViewModel,
) {
    val state = homeViewModel.state
    val worker: Worker? = state.worker
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            GlideImage(
//                model = worker?.profilePictureUrl ?: "https://http.cat/images/200.jpg",
//                contentDescription = "Profile Image",
//                modifier = Modifier.size(128.dp)
//            )
//            Text("Profile Screen")
//            Text("Name: ${worker?.firstName} ${worker?.lastName}")
//            Text("Email: ${worker?.email}")
//            Text("Phone: ${worker?.phone}")
//            Text("Username: ${worker?.username}")
//            Text("Date Joined: ${worker?.dateJoined}")
//            Text("Last Login: ${worker?.lastLogin}")
//            Text("Enabled: ${worker?.isEnabled}")
//            Text("Verification Code: ${worker?.verificationCode}")
//            Text("Verification Code Timestamp: ${worker?.verificationCodeTimestamp}")
//            Text("Firebase Token: ${worker?.firebaseToken}")
            ProfileScreenContent(worker)
        }


    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ProfileScreenContent(
    worker: Worker? = Worker(
        id = 132131,
        firstName = "John",
        lastName = "Doe",
        profilePictureUrl = "https://http.cat/images/200.jpg",
        email = "william.paterson@my-own-personal-domain.com",
        username = "johndoe",
        phone = "1234567890",
        isEnabled = true,
        profileVerified = true,
        verificationCode = "123456",
        verificationCodeTimestamp = "2022-01-01 00:00:00",
        firebaseToken = "abc123",
        dateJoined = "2022-01-01 00:00:00",
        lastLogin = "2022-01-01 12:00:00",
        latitude = 1.0,
        longitude = 2.0,
        bio = "I am a software engineer"
    )
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (worker != null && worker.profilePictureUrl.isNotEmpty() && worker.profilePictureUrl.isNotBlank() && worker.profilePictureUrl.contains(
                    "https"
                )
            ) {
                GlideImage(
                    model = worker.profilePictureUrl ?: "https://http.cat/images/200.jpg",
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .size(100.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFBDBDBD)), // gray placeholder
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
            Text("Hola,")
            Text(
                "${worker?.firstName} ${worker?.lastName}",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            InLineItem(
                name = "Número de trabajador",
                value = worker?.id.toString(),
                painter = painterResource(R.drawable.vector_back),
            )
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            InLineItem(
                name = "Documento de identidad o NIT:",
                value = worker?.id.toString(),
                icon = Icons.Outlined.Shield
            )
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            InLineItem(
                name = "Teléfono celular",
                value = worker?.phone ?: "",
                icon = Icons.Outlined.PhoneAndroid
            )
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            InLineItem(
                name = "Correo electrónico",
                value = worker?.email ?: "",
                icon = Icons.Outlined.Email
            )
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            InLineItem(
                name = "Miembro desde:",
                value = worker?.dateJoined?.toLocalDateTime()?.formatToReadableDate() ?: "",
                icon = Icons.Outlined.PersonAdd
            )
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            if (worker != null) {
                val formattedDate = worker.lastLogin.toLocalDateTime().formatToReadableDate()

                InLineItem(
                    name = "Última conexión: ",
                    value = formattedDate,
                    icon = Icons.Outlined.OnlinePrediction
                )
            }
        }
    }
}

@Composable
private fun InLineItem(
    name: String,
    value: String,
    icon: ImageVector? = null,
    painter: Painter? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        IconButton(
            onClick = { /*TODO*/ }
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Edit Profile"
                )
            } else if (painter != null) {
                Icon(
                    painter = painter,
                    contentDescription = "Edit Profile",
                    tint = Color.Black
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text("${name}", fontWeight = FontWeight.ExtraLight)
            Text("${value}", fontWeight = FontWeight.Bold)
        }
    }
}
