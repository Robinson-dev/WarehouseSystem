package cl.arriagada.microsaasadministrator.data.remote.supabase

/**
 * Configuración de Supabase para la aplicación.
 *
 * IMPORTANTE: Reemplazar estos valores con los datos de tu proyecto Supabase
 *
 * Pasos para obtener estas credenciales:
 * 1. Ir a https://supabase.com
 * 2. Crear un nuevo proyecto o usar existente
 * 3. En Settings → API → Project URL y API Keys
 * 4. Copiar los valores aquí
 */
object SupabaseConfig {

    // ========== CONFIGURACIÓN PRINCIPAL ==========

    /**
     * URL del proyecto Supabase
     * Formato: https://xxxxx.supabase.co
     *
     * Obtén este valor de:
     * Supabase Dashboard → Settings → API → Project URL
     */
    const val SUPABASE_URL = "https://qtfcbxwzuevcmyctcavc.supabase.co"

    /**
     * API Key pública (anon key)
     *
     * Obtén este valor de:
     * Supabase Dashboard → Settings → API → Project API keys
     * Usar la clave marcada como "anon"
     *
     * NOTA: Esta es la clave pública, es seguro compartirla en el cliente
     */
    const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InF0ZmNieHd6dWV2Y215Y3RjYXZjIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjQ0MjA4OTcsImV4cCI6MjA3OTk5Njg5N30.xtlZGxMcTQ83ugzy4onZrdfvYCDfkqU6lIUaPltXwuw"

    // ========== NOMBRES DE TABLAS ==========

    // Tabla de empresas
    const val TABLE_COMPANIES = "companies"

    // Tabla de bodegas
    const val TABLE_WAREHOUSES = "warehouses"

    // Tabla de productos
    const val TABLE_PRODUCTS = "products"

    // Tabla de movimientos
    const val TABLE_MOVEMENTS = "movements"

    // Tabla de usuarios
    const val TABLE_USERS = "users"

    // ========== CONFIGURACIÓN AVANZADA ==========

    /**
     * Timeout para solicitudes HTTP (en milisegundos)
     */
    const val NETWORK_TIMEOUT_MS = 30000L

    /**
     * Intervalo de sincronización automática (en milisegundos)
     * Sincronización cada 5 minutos
     */
    const val AUTO_SYNC_INTERVAL_MS = 5 * 60 * 1000L

    /**
     * Número máximo de reintentos para sincronización
     */
    const val MAX_SYNC_RETRIES = 3

    /**
     * Habilitar logs de Supabase (solo en debug)
     */
    const val ENABLE_SUPABASE_LOGS = true

    // ========== VERIFICACIÓN ==========

    /**
     * Verifica que las credenciales están configuradas
     */
    fun isConfigured(): Boolean {
        return SUPABASE_URL.isNotEmpty() &&
                SUPABASE_URL.startsWith("https://") &&
                SUPABASE_ANON_KEY.isNotEmpty() &&
                SUPABASE_ANON_KEY != "YOUR_ANON_KEY" &&
                SUPABASE_URL != "https://YOUR_PROJECT_ID.supabase.co"
    }

    /**
     * Obtiene mensaje de error si no está configurado
     */
    fun getConfigurationError(): String? {
        return when {
            SUPABASE_URL.contains("YOUR_PROJECT_ID") ->
                "❌ SUPABASE_URL no configurada. Reemplaza YOUR_PROJECT_ID con tu ID real"
            SUPABASE_ANON_KEY.contains("YOUR_ANON_KEY") ->
                "❌ SUPABASE_ANON_KEY no configurada. Reemplaza YOUR_ANON_KEY con tu clave real"
            !SUPABASE_URL.startsWith("https://") ->
                "❌ SUPABASE_URL debe empezar con https://"
            else -> null
        }
    }
}

/**
 * Errores de configuración
 */
sealed class SupabaseConfigException(message: String) : Exception(message) {
    object MissingUrl : SupabaseConfigException("Supabase URL no configurada")
    object MissingApiKey : SupabaseConfigException("Supabase API Key no configurada")
    object InvalidUrl : SupabaseConfigException("Supabase URL inválida")
}