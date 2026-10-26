package com.example.nexum_trabajador.ui.presenter.review

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.nexum_trabajador.ui.theme.Nexum_trabajadorTheme

/**
 * Diálogo interactivo de calificación para el trabajador hacia el cliente
 *
 * @author Santiago Varela Daza
 */
@Composable
fun ReviewDialog(
    targetName: String,
    targetRole: String = "el cliente",
    onSubmit: (rating: Int, comment: String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var rating by remember { mutableIntStateOf(5) }
    var comment by remember { mutableStateOf("") }

    val presetTags = remember {
        listOf(
            "Excelente cliente",
            "Puntual en el pago",
            "Trato respetuoso",
            "Lugar de trabajo adecuado",
            "Muy recomendado"
        )
    }
    var selectedTags by remember { mutableStateOf(setOf<String>()) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header con botón de cerrar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Calificar cliente",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "¿Cómo fue tu experiencia trabajando con $targetName ($targetRole)?",
                    fontSize = 14.sp,
                    color = Color(0xFF64748B),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Selector interactivo de estrellas
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 1..5) {
                        val isSelected = i <= rating
                        val starScale by animateFloatAsState(
                            targetValue = if (isSelected) 1.15f else 1.0f,
                            label = "star_scale_$i"
                        )
                        val starColor by animateColorAsState(
                            targetValue = if (isSelected) Color(0xFFFFB800) else Color(0xFFCBD5E1),
                            label = "star_color_$i"
                        )

                        Icon(
                            imageVector = if (isSelected) Icons.Filled.Star else Icons.Outlined.Star,
                            contentDescription = "Estrella $i",
                            tint = starColor,
                            modifier = Modifier
                                .size(40.dp)
                                .scale(starScale)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    rating = i
                                }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Etiqueta descriptiva del rating
                val ratingLabel = when (rating) {
                    5 -> "¡Excelente cliente!"
                    4 -> "Muy bueno"
                    3 -> "Aceptable"
                    2 -> "Regular"
                    else -> "Malo"
                }
                Text(
                    text = ratingLabel,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF009963)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Chips rápidos de aspectos positivos
                OptInFlowRow {
                    presetTags.forEach { tag ->
                        val isTagSelected = tag in selectedTags
                        FilterChip(
                            selected = isTagSelected,
                            onClick = {
                                selectedTags = if (isTagSelected) {
                                    selectedTags - tag
                                } else {
                                    selectedTags + tag
                                }
                            },
                            label = { Text(text = tag, fontSize = 12.sp) },
                            shape = CircleShape,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFE8F5E9),
                                selectedLabelColor = Color(0xFF009963)
                            ),
                            modifier = Modifier.padding(end = 6.dp, bottom = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Campo de comentario opcional
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    placeholder = {
                        Text(
                            text = "Deja una opinión sobre el cliente (opcional)...",
                            fontSize = 13.sp,
                            color = Color(0xFF94A3B8)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Botón Enviar
                Button(
                    onClick = {
                        val fullComment = buildString {
                            if (selectedTags.isNotEmpty()) {
                                append(selectedTags.joinToString(", "))
                                if (comment.isNotBlank()) append(". ")
                            }
                            if (comment.isNotBlank()) {
                                append(comment.trim())
                            }
                        }
                        onSubmit(rating, fullComment)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF009963),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Enviar calificación",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun OptInFlowRow(content: @Composable () -> Unit) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewDialogPreview() {
    Nexum_trabajadorTheme {
        ReviewDialog(
            targetName = "Andrea Gómez",
            targetRole = "el cliente",
            onSubmit = { _, _ -> },
            onDismiss = {}
        )
    }
}
