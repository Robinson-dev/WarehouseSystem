package cl.arriagada.microsaasadministrator.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val supabaseClient: SupabaseClient
) : ViewModel() {

    // ════════════════════════════════════════════════════════════
    // STATE
    // ════════════════════════════════════════════════════════════

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _passwordVisible = MutableStateFlow(false)
    val passwordVisible: StateFlow<Boolean> = _passwordVisible.asStateFlow()

    private val _validationErrors = MutableStateFlow<Map<String, String>>(emptyMap())
    val validationErrors: StateFlow<Map<String, String>> = _validationErrors.asStateFlow()

    // ════════════════════════════════════════════════════════════
    // EVENTOS DE UI
    // ════════════════════════════════════════════════════════════

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
        removeError("email")
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
        removeError("password")
    }

    fun togglePasswordVisibility() {
        _passwordVisible.value = !_passwordVisible.value
    }

    // ════════════════════════════════════════════════════════════
    // LOGIN
    // ════════════════════════════════════════════════════════════

    fun login() {
        // 1. Validar formulario
        if (!validateForm()) {
            Timber.w("Validación fallida")
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            try {
                Timber.d("Intentando login con: ${_email.value}")

                // 2. Autenticar con Supabase
                val userSession = supabaseClient.auth.signInWithPassword(
                    email = _email.value.trim(),
                    password = _password.value
                )

                // 3. Verificar que se autenticó
                if (userSession.user == null) {
                    _uiState.value = LoginUiState.Error(
                        message = "Error: Usuario no autenticado"
                    )
                    return@launch
                }

                Timber.d("Login exitoso: ${userSession.user?.id}")

                // 4. Retornar éxito con userId
                _uiState.value = LoginUiState.Success(
                    userId = userSession.user?.id ?: ""
                )

            } catch (e: Exception) {
                Timber.e(e, "Error en login")

                // Mapear errores comunes de Supabase
                val errorMessage = when {
                    e.message?.contains("Invalid login credentials", ignoreCase = true) == true ->
                        "Email o contraseña incorrectos"
                    e.message?.contains("User not found", ignoreCase = true) == true ->
                        "Usuario no encontrado"
                    e.message?.contains("Password should be", ignoreCase = true) == true ->
                        "Contraseña inválida"
                    e.message?.contains("Network error", ignoreCase = true) == true ->
                        "Error de conexión. Verifica tu internet"
                    else -> e.message ?: "Error desconocido"
                }

                addError("general", errorMessage)
                _uiState.value = LoginUiState.Error(message = errorMessage)
            }
        }
    }

    // ════════════════════════════════════════════════════════════
    // VALIDACIONES
    // ════════════════════════════════════════════════════════════

    private fun validateForm(): Boolean {
        clearAllErrors()
        var isValid = true

        // Email validación
        if (_email.value.isBlank()) {
            addError("email", "El email es obligatorio")
            isValid = false
        } else if (!isValidEmail(_email.value)) {
            addError("email", "El email no es válido")
            isValid = false
        }

        // Password validación
        if (_password.value.isBlank()) {
            addError("password", "La contraseña es obligatoria")
            isValid = false
        } else if (_password.value.length < 6) {
            addError("password", "Mínimo 6 caracteres")
            isValid = false
        }

        return isValid
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // ════════════════════════════════════════════════════════════
    // MANEJO DE ERRORES
    // ════════════════════════════════════════════════════════════

    private fun addError(field: String, message: String) {
        val current = _validationErrors.value.toMutableMap()
        current[field] = message
        _validationErrors.value = current
    }

    private fun removeError(field: String) {
        val current = _validationErrors.value.toMutableMap()
        current.remove(field)
        _validationErrors.value = current
    }

    private fun clearAllErrors() {
        _validationErrors.value = emptyMap()
    }
}

// ════════════════════════════════════════════════════════════
// UI STATE
// ════════════════════════════════════════════════════════════

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val userId: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}