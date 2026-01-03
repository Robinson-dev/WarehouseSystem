package com.inventory.presentation.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.arriagada.microsaasadministrator.presentation.auth.login.LoginUiState
import cl.arriagada.microsaasadministrator.presentation.auth.login.LoginViewModel

/**
 * LoginScreen
 *
 * Pantalla de autenticación simple
 *
 * Flujo:
 * 1. Usuario ingresa email/password
 * 2. Click "Iniciar Sesión"
 * 3. ViewModel valida localmente
 * 4. ViewModel llama a Supabase Auth
 * 5. Si éxito → onLoginSuccess(userId) → navega a Dashboard
 * 6. Si error → muestra mensaje en pantalla
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: (userId: String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val passwordVisible by viewModel.passwordVisible.collectAsStateWithLifecycle()
    val validationErrors by viewModel.validationErrors.collectAsStateWithLifecycle()

    // Manejar éxito de login
    LaunchedEffect(uiState) {
        if (uiState is LoginUiState.Success) {
            val userId = (uiState as LoginUiState.Success).userId
            onLoginSuccess(userId)
        }
    }

    // UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // ════════════════════════════════════════════════════════════
            // HEADER
            // ════════════════════════════════════════════════════════════

            Text(
                text = "📦 Inventario",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Gestión de Stock Operativo",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // ════════════════════════════════════════════════════════════
            // ERROR GENERAL
            // ════════════════════════════════════════════════════════════

            if (validationErrors.containsKey("general")) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = validationErrors["general"] ?: "",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // ════════════════════════════════════════════════════════════
            // EMAIL FIELD
            // ════════════════════════════════════════════════════════════

            OutlinedTextField(
                value = email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = { Text("Email") },
                placeholder = { Text("usuario@ejemplo.com") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                isError = validationErrors.containsKey("email"),
                supportingText = {
                    if (validationErrors.containsKey("email")) {
                        Text(
                            text = validationErrors["email"] ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ════════════════════════════════════════════════════════════
            // PASSWORD FIELD
            // ════════════════════════════════════════════════════════════

            OutlinedTextField(
                value = password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = { Text("Contraseña") },
                placeholder = { Text("••••••••") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    IconButton(onClick = { viewModel.togglePasswordVisibility() }) {
                        Icon(
                            imageVector = if (passwordVisible) {
                                Icons.Default.Visibility
                            } else {
                                Icons.Default.VisibilityOff
                            },
                            contentDescription = "Toggle password visibility"
                        )
                    }
                },
                isError = validationErrors.containsKey("password"),
                supportingText = {
                    if (validationErrors.containsKey("password")) {
                        Text(
                            text = validationErrors["password"] ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ════════════════════════════════════════════════════════════
            // LOGIN BUTTON
            // ════════════════════════════════════════════════════════════

            Button(
                onClick = { viewModel.login() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = uiState !is LoginUiState.Loading,
                shape = RoundedCornerShape(8.dp)
            ) {
                if (uiState is LoginUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        "Iniciar Sesión",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            // ════════════════════════════════════════════════════════════
            // ERROR DISPLAY (Si aplica)
            // ════════════════════════════════════════════════════════════

            if (uiState is LoginUiState.Error) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "❌ ${(uiState as LoginUiState.Error).message}",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // ════════════════════════════════════════════════════════════
            // FOOTER / HELP
            // ════════════════════════════════════════════════════════════

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Contacta con el administrador si no tienes cuenta",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}