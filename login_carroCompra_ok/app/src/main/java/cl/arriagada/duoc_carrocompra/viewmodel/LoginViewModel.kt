package cl.arriagada.duoc_carrocompra.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.arriagada.duoc_carrocompra.model.LoginRepository
import cl.arriagada.duoc_carrocompra.model.LoginResult
import cl.arriagada.duoc_carrocompra.utils.ValidationUtils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Data class que representa el estado completo de la UI de login
 *
 * ¿Por qué una data class?
 * - Nos permite agrupar TODOS los datos de la UI en un solo objeto
 * - Facilita pasar el estado completo a la View
 * - El compilador genera automáticamente copy(), equals(), toString()
 *
 * ¿Por qué valores por defecto (= "")?
 * - Permite crear instancias sin pasar parámetros: LoginUiState()
 * - Define el estado inicial de la pantalla
 */
data class LoginUiState(
    val email: String = "",                          // Texto del campo email, inicia vacío
    val password: String = "",                       // Texto del campo password, inicia vacío
    val emailError: String? = null,                  // Mensaje de error del email (null = sin error)
    val passwordError: String? = null,               // Mensaje de error del password (null = sin error)
    val isLoading: Boolean = false,                  // ¿Está cargando? false = no está cargando
    val loginResult: LoginResult = LoginResult.Idle, // Estado del resultado del login
    val passwordVisible: Boolean = false             // ¿Contraseña visible? false = oculta (asteriscos)
)

/**
 * ViewModel del login que maneja toda la logica de presentación
 *
 * Extiende de ViewModel para:
 * - Sobrevivir cambios de configuración o la rotacion del telefono
 * - Tener un ciclo de vida propio
 * - Cancelar operaciones automaticamente cuando se destruye
 */
class LoginViewModel : ViewModel() {

    // Instancia del repositorio para acceder a la lógica de negocio
    // En una app real se inyectaría con Hilt/Dagger (inyección de dependencias)
    private val repository = LoginRepository()

    /**
     * Estado mutable PRIVADO - Solo el ViewModel puede modificarlo
     *
     * MutableStateFlow es un flujo reactivo que:
     * - Emite valores cuando cambian
     * - Siempre tiene un valor inicial (LoginUiState())
     * - Es HOT (siempre está activo, aunque nadie lo observe)
     *
     * ¿Por qué MutableStateFlow?
     * - Permite modificar el estado con: _uiState.value = nuevoEstado
     * - Los observadores (View) reciben automáticamente los cambios
     * - Thread-safe: seguro para usar desde múltiples corrutinas
     */
    private val _uiState = MutableStateFlow(LoginUiState())

    /**
     * Estado público INMUTABLE - La View solo puede observarlo
     *
     * StateFlow es la versión de solo lectura de MutableStateFlow
     *
     * ¿Por qué esta separación privado/público?
     * - Principio de encapsulación: solo el ViewModel modifica el estado
     * - La View no puede modificar directamente, solo observar
     * - Evita bugs: la UI no puede poner el estado en un estado inválido
     *
     * asStateFlow() convierte MutableStateFlow → StateFlow (inmutable)
     */
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    /**
     * Se ejecuta cuando el usuario escribe en el campo de email
     * @param email Nuevo valor del email
     */
    fun onEmailChange(email: String) {
        // Actualizamos el estado con copy() - Inmutabilidad
        // NO modificamos el objeto actual, creamos uno NUEVO con los cambios
        _uiState.value = _uiState.value.copy(
            email = email,        // Actualizamos el email con el nuevo valor
            emailError = null     // Limpiamos el error al escribir (UX: feedback inmediato)
        )
        // copy() crea una copia del objeto actual pero cambiando solo los campos especificados
        // Los demás campos (password, isLoading, etc.) quedan igual
    }

    /**
     * Se ejecuta cuando el usuario escribe en el campo de password
     * @param password Nuevo valor del password
     */
    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,   // Actualizamos el password
            passwordError = null   // Limpiamos el error (UX: el usuario está corrigiendo)
        )
    }

    /**
     * Alterna la visibilidad de la contraseña entre mostrar y ocultar
     * Ejemplo: si passwordVisible = false, lo cambia a true (y viceversa)
     */
    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(
            // El operador ! niega el valor booleano
            // Si es false → true, si es true → false
            passwordVisible = !_uiState.value.passwordVisible
        )
    }
    /**
     * Valida todos los campos del formulario
     * @return true si todos los campos son válidos, false si hay errores
     *
     * Función PRIVADA porque es lógica interna del ViewModel
     * La View no necesita llamarla directamente
     */
    private fun validateFields(): Boolean {
        // Obtenemos mensajes de error usando ValidationUtils (singleton)
        // Si el campo es válido, devuelve null. Si es inválido, devuelve el mensaje
        val emailError = ValidationUtils.getEmailErrorMessage(_uiState.value.email)
        val passwordError = ValidationUtils.getPasswordErrorMessage(_uiState.value.password)

        // Actualizamos el estado con los errores (si existen)
        // La UI mostrará estos mensajes debajo de cada campo
        _uiState.value = _uiState.value.copy(
            emailError = emailError,        // null = válido, String = mensaje de error
            passwordError = passwordError   // null = válido, String = mensaje de error
        )

        // Retornamos true SOLO si ambos errores son null (campos válidos)
        // Si hay al menos un error, retorna false
        return emailError == null && passwordError == null
    }

    /**
     * Se ejecuta cuando el usuario hace click en el botón de login
     *
     * Flujo completo:
     * 1. Valida los campos antes de hacer la petición
     * 2. Si son válidos, activa el estado de loading
     * 3. Llama al repository (operación asíncrona)
     * 4. Actualiza el estado con el resultado (éxito o error)
     */
    fun onLoginClick() {
        // PASO 1: Validar campos primero
        if (!validateFields()) {
            // Si hay errores, no continuamos. Los errores ya están en el estado
            // La UI ya muestra los mensajes de error debajo de los campos
            return  // salimos de la función
        }

        // PASO 2: Iniciar operación asíncrona con viewModelScope
        // ¿Qué es viewModelScope?
        // - Es un CoroutineScope vinculado al ciclo de vida del ViewModel
        // - Se cancela automáticamente cuando el ViewModel se destruye
        // - Evita memory leaks: si el usuario sale de la pantalla, la operación se cancela
        viewModelScope.launch {
            // launch crea una nueva corrutina (como async/await en JavaScript)

            // Activamos el estado de loading ANTES de la operación
            _uiState.value = _uiState.value.copy(
                isLoading = true,                  // Muestra spinner en el botón
                loginResult = LoginResult.Loading  // Cambia el estado a Loading
            )

            // PASO 3: Llamar al repository (función suspend)
            // Esta línea "pausa" aquí hasta que login() termine
            // Pero NO bloquea el hilo principal (la UI sigue respondiendo)
            val result = repository.login(
                email = _uiState.value.email,      // Tomamos el email del estado actual
                password = _uiState.value.password // Tomamos el password del estado actual
            )
            // Después de 1.5 segundos (el delay en el repository), result contendrá
            // LoginResult.Success o LoginResult.Error

            // PASO 4: Actualizamos el estado con el resultado final
            _uiState.value = _uiState.value.copy(
                isLoading = false,    // Desactivamos el loading (oculta spinner)
                loginResult = result  // Puede ser Success o Error
            )
            // La View observa este cambio y reacciona (muestra mensaje de éxito/error)
        }
    }

    /**
     * Resetea el resultado del login al estado inicial (Idle)
     * Útil para cerrar mensajes de éxito o error cuando el usuario hace click en "OK"
     *
     * Ejemplo de uso: El usuario ve un error, hace click en "OK" para cerrarlo
     * → La UI llama resetLoginResult()
     * → loginResult vuelve a Idle
     * → El mensaje de error desaparece
     */
    fun resetLoginResult() {
        _uiState.value = _uiState.value.copy(
            loginResult = LoginResult.Idle  // Vuelve al estado inicial (sin mensaje)
        )
    }
}
