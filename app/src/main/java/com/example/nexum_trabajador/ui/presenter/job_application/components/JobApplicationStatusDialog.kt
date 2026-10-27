package com.example.nexum_trabajador.ui.presenter.job_application.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.nexum_trabajador.ui.presenter.job_application.DEFAULT_MAX_RETRIES

/**
 * Modal que muestra el estado de la postulación al hacer clic en "Enviar Propuesta".
 * Soporta los estados: Enviando (loading), Exitoso (success) y Fallido (error con opción de reintentar).
 *
 * Incluye límite configurable de reintentos ([maxRetries]) para evitar spam en el backend.
 */
@Composable
fun JobApplicationStatusDialog(
    isSubmitting: Boolean,
    isSuccess: Boolean,
    errorMessage: String,
    onAcceptSuccess: () -> Unit,
    onRetry: () -> Unit,
    onDismissError: () -> Unit,
    retryCount: Int = 0,
    maxRetries: Int = DEFAULT_MAX_RETRIES,
    primaryColor: Color = Color(0xFF4A7D29),
    modifier: Modifier = Modifier
) {
    val isVisible = isSubmitting || isSuccess || errorMessage.isNotBlank()
    if (!isVisible) return

    Dialog(
        onDismissRequest = {
            if (!isSubmitting) {
                if (isSuccess) {
                    onAcceptSuccess()
                } else {
                    onDismissError()
                }
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = !isSubmitting,
            dismissOnClickOutside = !isSubmitting
        )
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when {
                    isSubmitting -> {
                        // Estado: Enviando solicitud
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .background(primaryColor.copy(alpha = 0.1f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = primaryColor,
                                strokeWidth = 3.5.dp,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Text(
                            text = "Enviando propuesta...",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "Estamos registrando tu propuesta para el cliente. Por favor, espera un momento.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 14.sp,
                                color = Color.Gray,
                                lineHeight = 20.sp
                            ),
                            textAlign = TextAlign.Center
                        )
                    }

                    isSuccess -> {
                        // Estado: Solicitud exitosa
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .background(primaryColor.copy(alpha = 0.12f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = "Éxito",
                                tint = primaryColor,
                                modifier = Modifier.size(46.dp)
                            )
                        }

                        Text(
                            text = "¡Propuesta enviada con éxito!",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "Tu propuesta ha sido enviada al cliente satisfactoriamente. Te notificaremos cuando haya una respuesta sobre tu solicitud.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 14.sp,
                                color = Color(0xFF4A4A4A),
                                lineHeight = 20.sp
                            ),
                            textAlign = TextAlign.Center
                        )

                        Button(
                            onClick = onAcceptSuccess,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                        ) {
                            Text(
                                text = "Aceptar",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    errorMessage.isNotBlank() -> {
                        // Estado: Solicitud fallida
                        val errorColor = MaterialTheme.colorScheme.error
                        val remainingRetries = (maxRetries - retryCount).coerceAtLeast(0)
                        val canRetry = remainingRetries > 0

                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .background(
                                    (if (canRetry) errorColor else Color(0xFFFFA000)).copy(alpha = 0.12f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (canRetry) Icons.Filled.Error else Icons.Filled.Warning,
                                contentDescription = if (canRetry) "Error" else "Límite alcanzado",
                                tint = if (canRetry) errorColor else Color(0xFFFFA000),
                                modifier = Modifier.size(46.dp)
                            )
                        }

                        Text(
                            text = if (canRetry) "No se pudo enviar la propuesta" else "Límite de intentos alcanzado",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = if (!canRetry) {
                                "Has alcanzado el límite máximo de $maxRetries intentos permitidos para enviar esta propuesta. Por favor, verifica tu conexión o intenta más tarde."
                            } else {
                                errorMessage.ifBlank { "Ocurrió un error inesperado al enviar la solicitud. Por favor intenta de nuevo." }
                            },
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 14.sp,
                                color = Color(0xFF4A4A4A),
                                lineHeight = 20.sp
                            ),
                            textAlign = TextAlign.Center
                        )

                        if (canRetry && retryCount > 0) {
                            Surface(
                                color = Color(0xFFF3F4F6),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "Intento $retryCount de $maxRetries ($remainingRetries restantes)",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.DarkGray,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (canRetry) {
                                Button(
                                    onClick = onRetry,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                                ) {
                                    Text(
                                        text = "Reintentar",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }

                                OutlinedButton(
                                    onClick = onDismissError,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = "Cerrar",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.DarkGray
                                    )
                                }
                            } else {
                                Button(
                                    onClick = onDismissError,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                                ) {
                                    Text(
                                        text = "Entendido / Cerrar",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
