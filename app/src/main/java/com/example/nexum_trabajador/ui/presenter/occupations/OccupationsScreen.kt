package com.example.nexum_trabajador.ui.presenter.occupations

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nexum_trabajador.data.category.remote.payload.res.CategoryRes
import com.example.nexum_trabajador.data.certification.remote.payload.res.CertificationRes
import com.example.nexum_trabajador.data.job_occupation.remote.payload.res.JobOccupationRes
import com.example.nexum_trabajador.data.skill_set.remote.payload.res.SkillSetRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OccupationsScreen(
    viewModel: OccupationsViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state = viewModel.state
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onEvent(OccupationsEvent.ClearMessages)
        }
    }

    LaunchedEffect(state.successMessage) {
        state.successMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onEvent(OccupationsEvent.ClearMessages)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Mis Profesiones",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(OccupationsEvent.OpenAddDialog) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Profesión")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoading && state.occupations.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.occupations.isEmpty()) {
                EmptyOccupationsView(
                    onAddClicked = { viewModel.onEvent(OccupationsEvent.OpenAddDialog) },
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    Icons.Filled.Work,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(28.dp)
                                )
                                Text(
                                    text = "Gestiona aquí tus profesiones, las habilidades clave y los certificados que respaldan cada una.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    items(state.occupations, key = { it.id }) { occupation ->
                        OccupationCard(
                            occupation = occupation,
                            onToggleStatus = { viewModel.onEvent(OccupationsEvent.ToggleStatus(occupation)) },
                            onDelete = { viewModel.onEvent(OccupationsEvent.DeleteOccupation(occupation.id)) },
                            onAddSkill = { viewModel.onEvent(OccupationsEvent.OpenAddSkillDialog(occupation.id)) },
                            onRemoveSkill = { skillId -> viewModel.onEvent(OccupationsEvent.RemoveSkill(occupation.id, skillId)) },
                            onAddCert = { viewModel.onEvent(OccupationsEvent.OpenAddCertDialog(occupation.id)) },
                            onDeleteCert = { certId -> viewModel.onEvent(OccupationsEvent.DeleteCert(occupation.id, certId)) },
                            onOpenCertDocument = { url ->
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }

            // Dialog for adding profession
            if (state.showAddDialog) {
                AddOccupationDialog(
                    state = state,
                    onCategorySelected = { viewModel.onEvent(OccupationsEvent.CategorySelected(it)) },
                    onNameChanged = { viewModel.onEvent(OccupationsEvent.NameChanged(it)) },
                    onDescriptionChanged = { viewModel.onEvent(OccupationsEvent.DescriptionChanged(it)) },
                    onYearsChanged = { viewModel.onEvent(OccupationsEvent.YearsExperienceChanged(it)) },
                    onDismiss = { viewModel.onEvent(OccupationsEvent.DismissDialog) },
                    onSubmit = { viewModel.onEvent(OccupationsEvent.SubmitCreate) }
                )
            }

            // Dialog for adding skill
            if (state.showAddSkillDialog) {
                AddSkillDialog(
                    state = state,
                    onNameChanged = { viewModel.onEvent(OccupationsEvent.SkillNameChanged(it)) },
                    onDismiss = { viewModel.onEvent(OccupationsEvent.DismissSkillDialog) },
                    onSubmit = { viewModel.onEvent(OccupationsEvent.SubmitAddSkill) }
                )
            }

            // Dialog for adding certification
            if (state.showAddCertDialog) {
                AddCertificationDialog(
                    state = state,
                    onNameChanged = { viewModel.onEvent(OccupationsEvent.CertNameChanged(it)) },
                    onIssuedByChanged = { viewModel.onEvent(OccupationsEvent.CertIssuedByChanged(it)) },
                    onFileSelected = { viewModel.onEvent(OccupationsEvent.CertFileSelected(it)) },
                    onDismiss = { viewModel.onEvent(OccupationsEvent.DismissCertDialog) },
                    onSubmit = { viewModel.onEvent(OccupationsEvent.SubmitAddCert) }
                )
            }
        }
    }
}

@Composable
private fun EmptyOccupationsView(
    onAddClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Outlined.WorkOutline,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Text(
            text = "Sin profesiones registradas",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Text(
            text = "Agrega tus especialidades y categorías laborales para que los clientes cercanos puedan encontrarte.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            lineHeight = 20.sp
        )
        Button(
            onClick = onAddClicked,
            shape = RoundedCornerShape(20.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Registrar profesión")
        }
    }
}

@Composable
private fun OccupationCard(
    occupation: JobOccupationRes,
    onToggleStatus: () -> Unit,
    onDelete: () -> Unit,
    onAddSkill: () -> Unit,
    onRemoveSkill: (Long) -> Unit,
    onAddCert: () -> Unit,
    onDeleteCert: (Long) -> Unit,
    onOpenCertDocument: (String) -> Unit
) {
    val isActive = occupation.isActive ?: true

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (isActive) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header: Category and Status Chip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = occupation.categoryName ?: "General",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isActive) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                ) {
                    Text(
                        text = if (isActive) "Activa" else "Inactiva",
                        color = if (isActive) Color(0xFF2E7D32) else Color(0xFFC62828),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            // Title & Experience
            Text(
                text = occupation.name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (occupation.yearsExperience != null && occupation.yearsExperience > 0) {
                Text(
                    text = "💼 ${occupation.yearsExperience} años de experiencia",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
            }

            if (!occupation.description.isNullOrBlank()) {
                Text(
                    text = occupation.description,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            // --- SKILLS SECTION ---
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFF57C00),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Habilidades (${occupation.skills.size})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    TextButton(
                        onClick = onAddSkill,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+ Habilidad", fontSize = 12.sp)
                    }
                }

                if (occupation.skills.isEmpty()) {
                    Text(
                        text = "Agrega habilidades técnicas para resaltar en las búsquedas.",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(occupation.skills, key = { it.id }) { skill ->
                            SkillChip(
                                skill = skill,
                                onRemove = { onRemoveSkill(skill.id) }
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            // --- CERTIFICATIONS SECTION ---
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            Icons.Default.School,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Certificados (${occupation.certifications.size})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    TextButton(
                        onClick = onAddCert,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+ Certificado", fontSize = 12.sp)
                    }
                }

                if (occupation.certifications.isEmpty()) {
                    Text(
                        text = "Adjunta títulos o certificaciones para verificar tus competencias.",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        occupation.certifications.forEach { cert ->
                            CertificationItem(
                                cert = cert,
                                onOpenDocument = { cert.certificationUrl?.let { onOpenCertDocument(it) } },
                                onDelete = { onDeleteCert(cert.id) }
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            // Footer: Toggle status and delete
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Switch(
                        checked = isActive,
                        onCheckedChange = { onToggleStatus() }
                    )
                    Text(
                        text = if (isActive) "Recibiendo ofertas" else "Pausada",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isActive) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = Color(0xFFE53935).copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

@Composable
private fun SkillChip(
    skill: SkillSetRes,
    onRemove: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 1.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 10.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = skill.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(18.dp)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Quitar",
                    modifier = Modifier.size(13.dp),
                    tint = Color.Gray
                )
            }
        }
    }
}

@Composable
private fun CertificationItem(
    cert: CertificationRes,
    onOpenDocument: () -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    Icons.Default.School,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Column {
                    Text(
                        text = cert.name,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!cert.issuedBy.isNullOrBlank()) {
                        Text(
                            text = "Expedido por: ${cert.issuedBy}",
                            fontSize = 11.sp,
                            color = Color.Gray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (!cert.certificationUrl.isNullOrBlank()) {
                    IconButton(
                        onClick = onOpenDocument,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.OpenInNew,
                            contentDescription = "Ver documento",
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        modifier = Modifier.size(18.dp),
                        tint = Color(0xFFE53935).copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

// --- DIALOGS ---

@Composable
private fun AddSkillDialog(
    state: OccupationsState,
    onNameChanged: (String) -> Unit,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Agregar Habilidad Clave",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Text(
                    text = "Ingresa una habilidad técnica o destreza (ej: Soldadura MIG, Drywall, Cableado estructurado, etc.)",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    lineHeight = 18.sp
                )

                OutlinedTextField(
                    value = state.newSkillName,
                    onValueChange = onNameChanged,
                    label = { Text("Nombre de la habilidad *") },
                    placeholder = { Text("Ej. Instalación de pisos cerámicos") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onSubmit,
                        enabled = !state.isSavingSkill && state.newSkillName.isNotBlank(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (state.isSavingSkill) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Agregar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AddCertificationDialog(
    state: OccupationsState,
    onNameChanged: (String) -> Unit,
    onIssuedByChanged: (String) -> Unit,
    onFileSelected: (Uri) -> Unit,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit
) {
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onFileSelected(it) }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Nuevo Certificado o Título",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                OutlinedTextField(
                    value = state.certName,
                    onValueChange = onNameChanged,
                    label = { Text("Nombre del certificado / diploma *") },
                    placeholder = { Text("Ej. Técnico Electricista CONTE") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = state.certIssuedBy,
                    onValueChange = onIssuedByChanged,
                    label = { Text("Institución emisora (opcional)") },
                    placeholder = { Text("Ej. SENA, Ministerio, Academia...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                // Document / File upload section
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Archivo o soporte *",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )

                        if (state.isUploadingCertFile) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                                Text("Subiendo documento a la nube...", fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                            }
                        } else if (state.certUploadedUrl != null) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF2E7D32),
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "¡Documento cargado correctamente!",
                                    fontSize = 13.sp,
                                    color = Color(0xFF2E7D32),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        } else {
                            OutlinedButton(
                                onClick = { filePickerLauncher.launch("*/*") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.AttachFile, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Adjuntar archivo (PDF o Imagen)")
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onSubmit,
                        enabled = !state.isSavingCert && !state.isUploadingCertFile && state.certName.isNotBlank() && state.certUploadedUrl != null,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (state.isSavingCert) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Guardar Certificado")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddOccupationDialog(
    state: OccupationsState,
    onCategorySelected: (CategoryRes) -> Unit,
    onNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onYearsChanged: (String) -> Unit,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Nueva Profesión",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                // Category Dropdown
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = state.selectedCategory?.name ?: "Seleccionar Categoría",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoría de servicio *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        state.categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name ?: "") },
                                onClick = {
                                    onCategorySelected(category)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // Occupation Title / Name
                OutlinedTextField(
                    value = state.occupationName,
                    onValueChange = onNameChanged,
                    label = { Text("Título de la profesión / oficio *") },
                    placeholder = { Text("Ej. Electricista residencial") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                // Years of Experience
                OutlinedTextField(
                    value = state.yearsExperience,
                    onValueChange = onYearsChanged,
                    label = { Text("Años de experiencia") },
                    placeholder = { Text("Ej. 3") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                // Description
                OutlinedTextField(
                    value = state.occupationDescription,
                    onValueChange = onDescriptionChanged,
                    label = { Text("Descripción (opcional)") },
                    placeholder = { Text("Detalla tus conocimientos, herramientas y experiencia...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 3,
                    maxLines = 4
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onSubmit,
                        enabled = !state.isSaving,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (state.isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Guardar")
                        }
                    }
                }
            }
        }
    }
}
