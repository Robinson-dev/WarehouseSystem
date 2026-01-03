package cl.arriagada.duoc_carrocompra.model
import kotlinx.coroutines.delay

/**
 * Repositorio que maneja la lógica de negocio del login
 *
 * En una aplicación real, este repositorio haría llamadas a una api rest
 * Para este ejemplo, usamos datos locales y simulamos un delay de red
 * tal cual como en la prueba 1
 */
class LoginRepository {

    /**
     * vamos a crear una lista de usuarios válidos para testing
     * En una app real, esto debe de una base de datos o api, pero esto es una prueba
    listOf usaremos listOf(vararg elements: T) para pasar cantidades variables de argumentos
     */
    private val validUsers = listOf(
        User(email = "admin@carrocompra.com", password = "123456"),
        User(email = "usuario@test.com", password = "password"),
        User(email = "demo@demo.com", password = "demo123")
    )
    /**
     * Intenta hacer login con las credenciales proporcionadas
     * Esta función es 'suspend' porque realiza una operación asíncrona
     * Simula una llamada a API con un delay de 1.5 segundos
     * ademas devuelve loginReturn
     * @param email Email del usuario
     * @param password Contraseña del usuario
     * @return LoginResult con el resultado de la operación
     */
    suspend fun login(email: String, password: String): LoginResult {
        return try {
            // Simulamos delay de red, como si fuera una llamada http request
            delay(1500)

            // buscamos el usuario en la lista de usuarios validos
            // para eso vamos a recorrer esta lista e it es la forma en la que la recorremos
            val user = validUsers.find {
                // comparamos el email e ignoreCase indica que no importa mayusculas y minusculas
                it.email.equals(email, ignoreCase = true) &&
                        // y ademas && comparamos la contrasena exacta
                        it.password == password
            }

            // retornamos el resultado segun si encontramos el usuario
            if (user != null) {
                LoginResult.Success(user)
            } else {
                LoginResult.Error("email o contraseña incorrectos")
            }
        } catch (e: Exception) {
            // en caso de error, retornamos un LoginResult.Error
            LoginResult.Error("Error al iniciar sesión: ${e.message}")
        }
    }
}