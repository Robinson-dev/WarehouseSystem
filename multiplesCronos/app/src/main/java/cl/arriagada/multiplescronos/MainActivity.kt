package cl.arriagada.multiplescronos

import android.os.Bundle
import androidx.activity.ComponentActivity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cl.arriagada.multiplescronos.app.TimerManager

// Importa tu gestor y tu primera vista de Compose
import cl.arriagada.multiplescronos.ui.TimerListScreen
import cl.arriagada.multiplescronos.core.Accion
import cl.arriagada.multiplescronos.core.Comando

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Inicia los hilos una sola vez cuando la aplicación arranca.
        // El objeto TimerManager es un Singleton, así que solo se inicializa una vez.
        TimerManager.startThreads()

        // 2. Define la interfaz de usuario
        setContent {
            AppTheme { // Envuelve tu app en un tema (si lo tienes)
                AppNavigation()
            }
        }
    }
}

// ----------------------------------------------------------------------
// CONFIGURACIÓN DE LA NAVEGACIÓN (Simulada para empezar con la Lista)
// ----------------------------------------------------------------------
@Composable
fun AppNavigation() {
    // Por ahora, solo mostraremos la pantalla principal de la lista.
    // La navegación real se haría con una librería como Jetpack Navigation Compose.
    val cronometrosStateFlow = remember { TimerManager.cronometrosState }

    TimerListScreen(
        // Observa los datos del StateFlow
        cronometrosStateFlow = cronometrosStateFlow,

        // Define las acciones que envían comandos al Hilo Gestor
        onAddNewTimer = {
            // Esto navegaría a la vista CreateTimerScreen real.
            // Por ahora, solo enviamos un comando de creación de prueba
            TimerManager.sendCommand(Comando.Crear("Nuevo Crono Temporal"))
        },
        onControlTimer = { nombreCrono ->
            // Lógica simplificada: siempre intenta INICIAR/DETENER
            TimerManager.sendCommand(Comando.Gestionar(nombreCrono, Accion.Iniciar))
        }
    )
}