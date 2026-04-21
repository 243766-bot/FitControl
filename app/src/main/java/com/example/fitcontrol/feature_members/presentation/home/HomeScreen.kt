package com.example.fitcontrol.feature_members.presentation.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.fitcontrol.feature_members.domain.model.Member
import com.example.fitcontrol.feature_members.domain.model.MemberStatus

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    onNavigateToAddMember: () -> Unit,
    onNavigateToPayment: (String, String) -> Unit,
    onNavigateToInventory: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToEditMember: (String) -> Unit,
    onLogoutNavigate: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    var selectedMember by remember { mutableStateOf<Member?>(null) }
    var showOptions by remember { mutableStateOf(false) }

    // Diálogo de opciones para el socio
    if (showOptions && selectedMember != null) {
        AlertDialog(
            onDismissRequest = { showOptions = false },
            title = { Text(selectedMember?.name ?: "") },
            text = { Text("¿Qué deseas hacer con este socio?") },
            confirmButton = {
                Column {
                    TextButton(onClick = {
                        onNavigateToPayment(selectedMember!!.id, selectedMember!!.name)
                        showOptions = false
                    }) {
                        Icon(Icons.Default.Payments, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Cobrar Membresía")
                    }
                    TextButton(onClick = {
                        onNavigateToEditMember(selectedMember!!.id)
                        showOptions = false
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Editar Datos")
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.onDeleteMember(selectedMember!!)
                        showOptions = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Dar de Baja")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FitControl") },
                // Botón de Logout a la izquierda
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.onLogout()
                        onLogoutNavigate()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Cerrar Sesión"
                        )
                    }
                },
                actions = {
                    // NUEVO: Botón de Refresh
                    IconButton(onClick = { viewModel.refreshMembers() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Actualizar")
                    }
                    IconButton(onClick = onNavigateToInventory) {
                        Icon(Icons.Default.Inventory, contentDescription = "Inventario")
                    }
                    IconButton(onClick = onNavigateToReports) {
                        Icon(Icons.Default.BarChart, contentDescription = "Reportes")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAddMember) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Socio")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {

            if (state.members.isEmpty() && !state.isLoading) {
                Text(
                    text = "No hay socios registrados.",
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(Modifier.fillMaxSize()) {
                    items(state.members) { member ->
                        ListItem(
                            headlineContent = { Text(member.name, fontWeight = FontWeight.Bold) },
                            supportingContent = { Text(member.phone) },
                            leadingContent = {
                                AsyncImage(
                                    model = member.photoUrl,
                                    contentDescription = null,
                                    modifier = Modifier.size(50.dp),
                                    contentScale = ContentScale.Crop
                                )
                            },
                            trailingContent = {
                                val statusColor = if (member.status == MemberStatus.EXPIRED)
                                    MaterialTheme.colorScheme.error
                                else
                                    Color(0xFF4CAF50)

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = member.status.name,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = statusColor,
                                        fontWeight = FontWeight.Bold
                                    )

                                    member.daysLeft?.let { days ->
                                        Text(
                                            text = if (days >= 0) "$days días" else "Vencido",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = if (days < 0) MaterialTheme.colorScheme.error else Color.Gray
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.combinedClickable(
                                onClick = { onNavigateToPayment(member.id, member.name) },
                                onLongClick = {
                                    selectedMember = member
                                    showOptions = true
                                }
                            )
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
            }

            // Indicador de carga (Punto de la rúbrica)
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            // Mensaje de error
            state.error?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}