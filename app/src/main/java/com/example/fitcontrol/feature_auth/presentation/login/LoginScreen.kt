package com.example.fitcontrol.feature_auth.presentation.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fitcontrol.core.util.BiometricHelper
import com.example.fitcontrol.ui.components.FitButton
import com.example.fitcontrol.ui.components.FitTextField

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    
    val context = LocalContext.current
    val biometricHelper = remember { BiometricHelper(context) }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "FitControl",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Gestión Integral para Gimnasios",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        FitTextField(
            value = email,
            onValueChange = { email = it },
            label = "Correo electrónico"
        )

        FitTextField(value = password,
            onValueChange = { password = it },
            label = "Contraseña",
            isPassword = true
        )

        if (state.error != null) {
            Text(text = state.error!!, color = MaterialTheme.colorScheme.error)
        }

        FitButton(
            text = if (state.isLoading) "Cargando..." else "Iniciar Sesión",
            onClick = { viewModel.onLoginClick(email, password) },
            enabled = !state.isLoading
        )

        if (biometricHelper.isBiometricAvailable()) {
            OutlinedIconButton(
                onClick = {
                    biometricHelper.showBiometricPrompt(
                        activity = context as FragmentActivity,
                        onSuccess = { viewModel.onLoginClick(email, password) },
                        onError = { _, _ -> }
                    )
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(Icons.Default.Fingerprint, contentDescription = "Biometría")
            }
        }

        TextButton(onClick = onNavigateToRegister) {
            Text("¿No tienes cuenta? Regístrate aquí")
        }
    }
}