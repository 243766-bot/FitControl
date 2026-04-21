package com.example.fitcontrol.feature_members.presentation.edit_member

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fitcontrol.ui.components.FitButton
import com.example.fitcontrol.ui.components.FitTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMemberScreen(
    memberId: String,
    onNavigateBack: () -> Unit,
    onMemberUpdated: () -> Unit,
    viewModel: EditMemberViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }

    LaunchedEffect(memberId) {
        viewModel.loadMember(memberId)
    }

    LaunchedEffect(state.member) {
        state.member?.let {
            name = it.name
            phone = it.phone
            email = it.email
            birthDate = it.birthDate
        }
    }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onMemberUpdated()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Socio") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoading && state.member == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FitTextField(value = name, onValueChange = { name = it }, label = "Nombre completo")
                FitTextField(value = phone, onValueChange = { phone = it }, label = "Teléfono")
                FitTextField(value = email, onValueChange = { email = it }, label = "Correo electrónico")
                FitTextField(value = birthDate, onValueChange = { birthDate = it }, label = "Fecha de nacimiento")

                if (state.error != null) {
                    Text(text = state.error!!, color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.weight(1f))

                FitButton(
                    text = if (state.isLoading) "Guardando..." else "Actualizar Datos",
                    onClick = { 
                        viewModel.onUpdateMember(name, phone, email, birthDate) 
                    },
                    enabled = !state.isLoading
                )
            }
        }
    }
}