package com.example.nexum_trabajador.ui.presenter.job_application.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nexum_trabajador.data.job_application.remote.payload.req.AddMaterialReq
import com.example.nexum_trabajador.ui.presenter.job_application.JobApplicationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobApplicationMaterialsScreen(
    viewModel: JobApplicationViewModel,
    onNavigateNext: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var editingIndex by remember { mutableStateOf<Int?>(null) }

    val totalCost = state.materials.sumOf { it.quantity * it.unitPrice }
    val primaryGreen = Color(0xFF4A7D29)

    Scaffold(
        topBar = {
            val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Cotización Materiales",
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
            Text(
                text = "Paso 3 de 5",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
            )

            // Header: Total
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Total:", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(
                    text = "$ ${totalCost.toInt()}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }

            // Material List
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { Spacer(modifier = Modifier.height(4.dp)) }

                if (state.materials.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 48.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                Icons.Filled.ShoppingCart,
                                contentDescription = null,
                                tint = Color(0xFFE0E0E0),
                                modifier = Modifier.size(64.dp)
                            )
                            Text(
                                "Sin materiales agregados",
                                color = Color.LightGray,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                "Pulsa ➕ para agregar materiales (opcional)",
                                color = Color.LightGray,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                itemsIndexed(state.materials) { index, mat ->
                    MaterialItemCard(
                        material = mat,
                        onDelete = { viewModel.removeMaterial(index) },
                        onEdit = {
                            editingIndex = index
                        }
                    )
                }

                item { Spacer(modifier = Modifier.height(4.dp)) }
            }

            // Bottom area: Siguiente button + FAB
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
                    Icon(Icons.Filled.Add, contentDescription = "Agregar material")
                }
            }
        }
    }

    if (showDialog) {
        AddMaterialDialog(
            onDismiss = { showDialog = false },
            onConfirm = { name, quantity, price, desc, type ->
                viewModel.addMaterial(
                    AddMaterialReq(
                        name = name,
                        description = desc,
                        type = type,
                        quantity = quantity,
                        unitPrice = price
                    )
                )
                showDialog = false
            }
        )
    }

    editingIndex?.let { idx ->
        val mat = state.materials.getOrNull(idx)
        if (mat != null) {
            AddMaterialDialog(
                initialName = mat.name,
                initialQuantity = mat.quantity.toString(),
                initialPrice = mat.unitPrice.toString(),
                initialDesc = mat.description,
                initialType = mat.type,
                onDismiss = { editingIndex = null },
                onConfirm = { name, quantity, price, desc, type ->
                    viewModel.editMaterial(
                        idx,
                        AddMaterialReq(
                            name = name,
                            description = desc,
                            type = type,
                            quantity = quantity,
                            unitPrice = price
                        )
                    )
                    editingIndex = null
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MaterialItemCardPreview() {
    MaterialItemCard(
        material = AddMaterialReq(
            name = "Nombre material",
            description = "Descripcion material super largga para probar el espaciado del card y asegurarse que ocupe el espacio necesario",
            type = "Herramienta",
            quantity = 2,
            unitPrice = 25000.0,
        ),
        onDelete = {},
        onEdit = {}
    )
}

@Composable
fun MaterialItemCard(material: AddMaterialReq, onDelete: () -> Unit, onEdit: () -> Unit) {
    var expandedMenu by remember { mutableStateOf(false) }

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
            // Top Row: Title and Menu
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = material.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color.Black
                )
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
                            leadingIcon = { Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary) },
                            onClick = {
                                expandedMenu = false
                                onEdit()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Eliminar", color = MaterialTheme.colorScheme.error) },
                            leadingIcon = { Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error) },
                            onClick = {
                                expandedMenu = false
                                onDelete()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Description
            Text(
                text = material.description.ifEmpty { "Sin descripción" },
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(15.dp))

            // Details and Total line

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Precio/U : $ ${material.unitPrice.toInt()}",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.weight(1f)
                )
                if (material.type.isNotBlank()) {
                    SuggestionChip(
                        onClick = { },
                        label = { Text(material.type, fontSize = 10.sp) },
                        modifier = Modifier.height(24.dp),
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = Color(
                                0xFFF0F0F0
                            )
                        ),
                        border = null
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Cantidad : ${material.quantity}",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "$ ${(material.unitPrice * material.quantity).toInt()}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(end = 1.dp)
                )
            }
        }


    }
}

@Composable
fun AddMaterialDialog(
    initialName: String = "",
    initialQuantity: String = "",
    initialPrice: String = "",
    initialDesc: String = "",
    initialType: String = "",
    onDismiss: () -> Unit,
    onConfirm: (String, Int, Double, String, String) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var quantity by remember { mutableStateOf(initialQuantity) }
    var price by remember { mutableStateOf(initialPrice) }
    var type by remember { mutableStateOf(initialType) }
    var desc by remember { mutableStateOf(initialDesc) }
    var expandedType by remember { mutableStateOf(false) }

    val materialTypes = listOf("Herramienta", "Consumible", "Insumo", "Otro")

    val primaryGreen = Color(0xFF4A7D29)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = {
            Text(
                text = "Agregar Material",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Nombre
                Column {
                    Text("Nombre", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = { Text("Ingresar nombre") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        )
                    )
                }

                // Cantidad y Tipo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Cantidad", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        OutlinedTextField(
                            value = quantity,
                            onValueChange = { quantity = it },
                            placeholder = { Text("Ingresar cantidad") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color(0xFFE0E0E0)
                            )
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text("Tipo", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Box {
                            OutlinedTextField(
                                value = type,
                                onValueChange = {},
                                readOnly = true,
                                placeholder = { Text("Seleccione ...", fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = Color(0xFFE0E0E0)
                                ),
                                trailingIcon = {
                                    IconButton(onClick = { expandedType = !expandedType }) {
                                        Icon(
                                            Icons.Default.ArrowDropDown,
                                            contentDescription = "Expandir"
                                        )
                                    }
                                }
                            )
                            // Transparent Box over textfield to capture click for whole area
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clickable { expandedType = !expandedType }
                            )
                            DropdownMenu(
                                expanded = expandedType,
                                onDismissRequest = { expandedType = false },
                                modifier = Modifier.background(Color.White)
                            ) {
                                materialTypes.forEach { t ->
                                    DropdownMenuItem(
                                        text = { Text(t) },
                                        onClick = {
                                            type = t
                                            expandedType = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                // Precio
                Column {
                    Text("Precio", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        placeholder = { Text("Ingresar precio.") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        )
                    )
                }

                // Descripción
                Column {
                    Text("Descripción", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    OutlinedTextField(
                        value = desc,
                        onValueChange = { desc = it },
                        placeholder = { Text("Agregar descripción ...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
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
                    if (name.isNotBlank() && quantity.isNotBlank() && price.isNotBlank() && type.isNotBlank()) {
                        val q = quantity.toIntOrNull() ?: 1
                        val p = price.toDoubleOrNull() ?: 0.0
                        onConfirm(name, q, p, desc, type)
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
