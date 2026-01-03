package cl.arriagada.duoc_carrocompra.model

/**
 * Data class que representa el modelo de usuario
 * @param email email del usuario
 * @param password contraseña del usuario
 */
data class User(
    val email: String = "",
    val password: String = ""
)

/**
 * Sealed class que representa los posibles resultados de la operación de login
 * Sealed class es como un enum avanzado que permite diferentes tipos con datos asociados
 * vamos a crear 4 sub clases y estas heredaran de LoginResult() con : LoginResult
 * delimitando solo estas 4 subclases puedan ser de tipo LoginResult()
 * esto lo hacemos para que el compilador evite que se creen estados inesperados
 */
sealed class LoginResult {
    // Success y Error contienen datos, por ende seran de tipo data class
    // Login exitoso con datos del usuario
    data class Success(val user: User) : LoginResult()
    // Login fallido con mensaje de error
    data class Error(val message: String) : LoginResult()

    // aqui no guardamos datos, por ende crearemos objects
    // Login en progreso o cargando
    object Loading : LoginResult()

    // Estado inicial, o sin accion
    object Idle : LoginResult()
}