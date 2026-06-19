package com.example.nexum_trabajador.ui.presenter.job_application.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nexum_trabajador.data.job_application.remote.payload.req.AddTaskReq
import com.example.nexum_trabajador.ui.presenter.job_application.JobApplicationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobApplicationTasksScreen(
    viewModel: JobApplicationViewModel,
    onNavigateNext: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var editingIndex by remember { mutableStateOf<Int?>(null) }

    val primaryGreen = Color(0xFF4A7D29)

    Scaffold(
        topBar = {
            val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Tareas a realizar",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                windowInsets = WindowInsets(
                    top = (statusBarHeight - 12.dp).coerceAtLeast(0.dp)
                ),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryGreen,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Paso indicator + contador
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Paso 2 de 5", style = MaterialTheme.typography.labelMedium)
                if (state.tasks.isNotEmpty()) {
                    Text(
                        "${state.tasks.size} tarea${if (state.tasks.size != 1) "s" else ""}",
                        style = MaterialTheme.typography.labelMedium,
                        color = primaryGreen,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Lista de tareas
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { Spacer(modifier = Modifier.height(4.dp)) }

                if (state.tasks.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 48.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFFE0E0E0),
                                modifier = Modifier.size(64.dp)
                            )
                            Text(
                                "Sin tareas agregadas",
                                color = Color.LightGray,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                "Pulsa ➕ para agregar una tarea",
                                color = Color.LightGray,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                itemsIndexed(state.tasks) { index, task ->
                    TaskItemCard(
                        task = task,
                        onDelete = { viewModel.removeTask(index) },
                        onEdit = { editingIndex = index }
                    )
                }

                item { Spacer(modifier = Modifier.height(4.dp)) }
            }

            // Bottom row: Siguiente + FAB
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onNavigateNext,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryGreen)
                ) {
                    Text("Siguiente", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                FloatingActionButton(
                    onClick = { showDialog = true },
                    containerColor = primaryGreen,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Agregar tarea")
                }
            }
        }
    }

    // Diálogo: Agregar
    if (showDialog) {
        AddTaskDialog(
            onDismiss = { showDialog = false },
            onConfirm = { name, desc ->
                viewModel.addTask(AddTaskReq(taskName = name, taskDescription = desc))
                showDialog = false
            }
        )
    }

    // Diálogo: Editar
    editingIndex?.let { idx ->
        val task = state.tasks.getOrNull(idx)
        if (task != null) {
            AddTaskDialog(
                initialName = task.taskName,
                initialDesc = task.taskDescription,
                onDismiss = { editingIndex = null },
                onConfirm = { name, desc ->
                    viewModel.editTask(idx, AddTaskReq(taskName = name, taskDescription = desc))
                    editingIndex = null
                }
            )
        }
    }
}

// ── TaskItemCard ──────────────────────────────────────────────────────────────

@Composable
fun TaskItemCard(task: AddTaskReq, onDelete: () -> Unit, onEdit: () -> Unit) {
    var expandedMenu by remember { mutableStateOf(false) }
    val primaryGreen = Color(0xFF4A7D29)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Fila superior: nombre + menú
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = primaryGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = task.taskName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                }

                Box {
                    IconButton(
                        onClick = { expandedMenu = true },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            Icons.Filled.MoreVert,
                            contentDescription = "Opciones",
                            tint = Color.DarkGray
                        )
                    }
                    DropdownMenu(
                        expanded = expandedMenu,
                        onDismissRequest = { expandedMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Editar") },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Edit,
                                    contentDescription = "Editar",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            onClick = {
                                expandedMenu = false
                                onEdit()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Eliminar", color = MaterialTheme.colorScheme.error) },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Delete,
                                    contentDescription = "Eliminar",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            },
                            onClick = {
                                expandedMenu = false
                                onDelete()
                            }
                        )
                    }
                }
            }

            // Descripción
            if (task.taskDescription.isNotBlank()) {
                Text(
                    text = task.taskDescription,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

// ── AddTaskDialog ─────────────────────────────────────────────────────────────

@Composable
fun AddTaskDialog(
    initialName: String = "",
    initialDesc: String = "",
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var desc by remember { mutableStateOf(initialDesc) }
    val primaryGreen = Color(0xFF4A7D29)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = {
            Text(
                text = if (initialName.isEmpty()) "Agregar Tarea" else "Editar Tarea",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Nombre de la tarea
                Column {
                    Text("Nombre de la tarea", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = { Text("Ej: Instalación de cableado") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        singleLine = true
                    )
                }

                // Descripción
                Column {
                    Text("Descripción (opcional)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    OutlinedTextField(
                        value = desc,
                        onValueChange = { desc = it },
                        placeholder = { Text("Agregar descripción de la tarea...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        ),
                        maxLines = 4
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank()) {
                        onConfirm(name.trim(), desc.trim())
                    }
                }
            ) {
                Text(
                    "Aceptar",
                    color = primaryGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color.Gray, fontSize = 16.sp)
            }
        }
    )
}
