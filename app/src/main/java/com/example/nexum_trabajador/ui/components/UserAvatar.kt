package com.example.nexum_trabajador.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.signature.ObjectKey

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserAvatar(
    imageUrl: String?,
    name: String?,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    contentDescription: String = "Avatar",
    onClick: (() -> Unit)? = null
) {
    val cleanName = remember(name) {
        name?.replace("null", "")?.trim()?.takeIf { it.isNotBlank() } ?: "Usuario"
    }

    val fallbackUrl = remember(cleanName) {
        val parts = cleanName.split("\\s+".toRegex()).filter { it.isNotBlank() }
        val fn = parts.firstOrNull() ?: ""
        val ln = if (parts.size > 1) parts[1] else ""
        val nameToUse = if (fn.isEmpty() && ln.isEmpty()) "U" else if (ln.isEmpty()) fn else "$fn+$ln"
        "https://ui-avatars.com/api/?name=$nameToUse&background=random&format=png"
    }

    val validImgUrl = remember(imageUrl) {
        imageUrl?.takeIf {
            it.isNotBlank() && it != "NaN" && it != "null" && it.startsWith("http") && !it.contains("localhost") && !it.contains("127.0.0.1")
        }
    }

    val model = validImgUrl ?: fallbackUrl

    val initialLetter = remember(cleanName) {
        cleanName.firstOrNull { it.isLetter() }?.uppercaseChar()?.toString() ?: "U"
    }

    val fontSize = (size.value * 0.38f).coerceAtLeast(10f).sp

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Color(0xFFE2E8F0))
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initialLetter,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )
        GlideImage(
            model = model,
            contentDescription = contentDescription,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            requestBuilderTransform = {
                it.signature(ObjectKey(model))
                    .error(it.clone().load(fallbackUrl))
            }
        )
    }
}
