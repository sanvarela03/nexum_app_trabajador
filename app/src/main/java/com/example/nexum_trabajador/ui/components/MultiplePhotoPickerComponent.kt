package com.example.nexum_trabajador.ui.components

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @since 8/8/2025
 * @version 1.0
 */
@OptIn(ExperimentalGlideComposeApi::class, ExperimentalLayoutApi::class)
@Composable
fun MultiplePhotoPickerComponent(
    selectedImages: MutableList<Uri> = mutableStateListOf(),
    onAddImage: (Uri) -> Unit,
    onRemoveImage: (Int) -> Unit
) {
    Log.d("PhotoPickerComponent", "selectedImages: $selectedImages")
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uri ->
            uri?.let {
                uri.forEach {
                    onAddImage(it)
                }
            }
        }
    )
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FlowRow(
            modifier =
                Modifier
//                .border(
//                    BorderStroke(1.dp, Color(0xFFE6E6E6))
//                )
                    .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            OutlinedIconButton(
                modifier = Modifier
                    .defaultMinSize(100.dp, 100.dp),
                shape = RoundedCornerShape(12.dp),
                colors = IconButtonColors(
                    contentColor = Color(0xFFE6E6E6),
                    containerColor = Color.White,
                    disabledContainerColor = Color.White,
                    disabledContentColor = Color.Black,
                ),
                border = BorderStroke(1.dp, Color(0xFFE6E6E6)),

                onClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Photo",
                    modifier = Modifier
                        .size(40.dp),
                )
            }

            if (selectedImages.isNotEmpty()) {
                selectedImages.forEachIndexed() { index, uri ->
                    ImagePreview(
                        uri = uri,
                        onRemoveImage = onRemoveImage,
                        index = index
                    )
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalGlideComposeApi::class)
fun ImagePreview(
    uri: Any?,
    onRemoveImage: (Int) -> Unit,
    index: Int,
    contentScale: ContentScale = ContentScale.Crop,
    containerModifier: Modifier = Modifier
        .size(100.dp),
    contentModifier: Modifier = Modifier
        .clip(RoundedCornerShape(12.dp))
        .fillMaxSize()
) {
    Box(
        modifier = containerModifier,
//        contentAlignment = Alignment.Center
    ) {
        // Show image only if we actually have one
        if (uri != null && uri != "") {
            GlideImage(
                model = uri,
                contentDescription = "Selected Image",
                modifier = contentModifier,
                contentScale = contentScale
            )

            // Show remove icon only when an image is shown
            IconButton(
                onClick = { onRemoveImage(index) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(3.dp)
                    .clip(RoundedCornerShape(50))
                    .size(24.dp)
                    .background(Color.Black.copy(alpha = 0.6f))
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}


@Composable
@OptIn(ExperimentalGlideComposeApi::class)
fun ImagePreview(
    uri: Any?,
    onRemoveImage: (Int) -> Unit,
    index: Int,
    contentScale: ContentScale = ContentScale.Crop,
    modifier: Modifier = Modifier
        .clip(RoundedCornerShape(12.dp))
        .fillMaxSize()
) {
    Box(modifier = Modifier.size(100.dp)) {
        // Show image only if we actually have one
        if (uri != null && uri != "") {
            GlideImage(
                model = uri,
                contentDescription = "Selected Image",
                modifier = modifier,
                contentScale = contentScale
            )

            // Show remove icon only when an image is shown
            IconButton(
                onClick = { onRemoveImage(index) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(3.dp)
                    .clip(RoundedCornerShape(50))
                    .size(24.dp)
                    .background(Color.Black.copy(alpha = 0.6f))
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

