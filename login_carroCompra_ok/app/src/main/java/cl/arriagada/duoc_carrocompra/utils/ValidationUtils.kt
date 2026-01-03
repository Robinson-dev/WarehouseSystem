package cl.arriagada.duoc_carrocompra.utils
/**
 * La palabra object automaticamente crea un singleton
 * https://kotlinlang.org/docs/object-declarations.html
 * ventaja fundamental, es thread safe
 * esto nos permite que en una sola instancia, podamos tener todas las funciones de validacion
 * asi no creamos multiples instancias de este objeto de validacion
 * En Kotlin, 'object' crea un singleton, en otras palabras, solo existe una instancia
 */
object ValidationUtils {
    /**
     * Valida que el email tenga un formato correcto
     * @param email Email a validar
     * @return true si el email es válido, false si no
     */
    fun isValidEmail(email: String): Boolean {
        // Verificar que no esté vacío
        if (email.isBlank()) return false

        /*
         * Descomposición del regex:
        *
        * ^                      = Inicio de la cadena
        * [A-Za-z0-9+_.-]+       = Uno o más caracteres válidos antes del @ (letras, números, +, _, ., -)
        * @                      = Arroba literal
        * [A-Za-z0-9.-]+         = Uno o más caracteres para el dominio (letras, números, ., -)
        * \\.                    = Punto literal (escapado con \\)
        * [A-Za-z]{2,}           = Al menos 2 letras para la extensión (.com, .cl, etc.)
        * $                      = Fin de la cadena
        */

        // Regex para validar formato: usuario@dominio.extension
        // .toRegex() convierte el String en un objeto Regex que puede validar
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()

        // como ahora convertimos el emailRegex en un objeto, podemos validar la cadena email
        return emailRegex.matches(email)
    }
    /**
     * Valida que la contraseña tenga al menos 6 caracteres
     * @param password Contraseña a validar
     * @return true si la contraseña es válida, false si no
     */
    fun isValidPassword(password: String): Boolean {
        // el metodo length devuelve un int, pero al usar comparador, devuelve un booleano
        return password.length >= 6
    }
    /**
     * Obtiene el mensaje de error para email
     * @param email Email a validar
     * @return Mensaje de error o null si es válido
     */
    fun getEmailErrorMessage(email: String): String? {
        // recuerden que cuando devolvemos un when (switch case) es obligatorio agregar un else
        return when {
            // validamos si el email esta en blanco
            email.isBlank() -> "El email es requerido"

            // ahora validamos con isValidEmail() pero tenemos que usarlo con negacion
            // porque de lo contrario no entraria en el caso de error
            !isValidEmail(email) -> "Email inválido"
            else -> null
        }
    }
    /**
     * Obtiene el mensaje de error para contraseña
     * @param password Contraseña a validar
     * @return Mensaje de error o null si es válida
     */
    fun getPasswordErrorMessage(password: String): String? {
        return when {
            password.isBlank() -> "La contraseña es requerida"
            !isValidPassword(password) -> "La contraseña debe tener al menos 6 caracteres"
            else -> null
        }
    }
}