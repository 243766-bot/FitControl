package com.example.fitcontrol.feature_members.presentation.add_member

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.fitcontrol.core.util.camera.CameraCapture
import com.example.fitcontrol.ui.components.FitButton
import com.example.fitcontrol.ui.components.FitTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMemberScreen(
    onNavigateBack: () -> Unit,
    onMemberSaved: () -> Unit,
    viewModel: AddMemberViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    
    var showCamera by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            showCamera = true
        }
    }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onMemberSaved()
        }
    }

    if (showCamera) {
        CameraCapture(
            onImageCaptured = { uri ->
                viewModel.onImageCaptured(uri.toString())
                showCamera = false
            },
            onError = { showCamera = false }
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Registrar Nuevo Socio") },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .padding(bottom = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (state.capturedImageUri != null) {
                        AsyncImage(
                            model = Uri.parse(state.capturedImageUri),
                            contentDescription = "Foto del socio",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        OutlinedCard(
                            onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.CameraAlt, contentDescription = null)
                                    Text("Tomar Foto", style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }
                }

                FitTextField(value = name, onValueChange = { name = it }, label = "Nombre completo")
                FitTextField(value = phone, onValueChange = { phone = it }, label = "Teléfono")
                FitTextField(value = email, onValueChange = { email = it }, label = "Correo electrónico")
                FitTextField(value = birthDate, onValueChange = { birthDate = it }, label = "Fecha de nacimiento (DD/MM/AAAA)")

                if (state.error != null) {
                    Text(text = state.error!!, color = MaterialTheme.colorScheme.error)
                }

                FitButton(
                    text = if (state.isLoading) "Guardando..." else "Guardar Socio",
                    onClick = { viewModel.onSaveMember(name, phone, email, birthDate) },
                    enabled = !state.isLoading
                )
            }
        }
    }
}