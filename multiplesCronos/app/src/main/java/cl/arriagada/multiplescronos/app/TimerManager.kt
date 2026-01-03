package cl.arriagada.multiplescronos.app

import cl.arriagada.multiplescronos.core.Accion
import cl.arriagada.multiplescronos.core.Comando
import cl.arriagada.multiplescronos.model.Cronometro
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.concurrent.thread
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.LinkedBlockingQueue

object TimerManager {
    // ----------------------------------------------------------------------
    // 1. COMUNICACIÓN ENTRE HILOS (COLAS)
    // ----------------------------------------------------------------------
    private val colaCreacion = LinkedBlockingQueue<Comando.Crear>()
    private val colaGestion = LinkedBlockingQueue<Comando.Gestionar>()

    // ----------------------------------------------------------------------
    // 2. DATOS OBSERVABLES PARA LA UI (El nuevo 'Main' de datos)
    // ----------------------------------------------------------------------
    private val cronometros = ConcurrentHashMap<String, Cronometro>()
    private val _cronometrosState = MutableStateFlow<Map<String, Cronometro>>(emptyMap())
    // La UI observará este StateFlow para actualizarse automáticamente
    val cronometrosState: StateFlow<Map<String, Cronometro>> = _cronometrosState.asStateFlow()

    // ----------------------------------------------------------------------
    // 3. ENTRADA DE COMANDOS DESDE LA UI
    // ----------------------------------------------------------------------
    fun sendCommand(comando: Comando) {
        when (comando) {
            is Comando.Crear -> colaCreacion.put(comando)
            is Comando.Gestionar -> colaGestion.put(comando)
            is Comando.Salir -> {
                // En una app real, aquí se detendrían todos los hilos
                // Por ahora, solo cerramos la app
            }
        }
    }

    // ----------------------------------------------------------------------
    // 4. ORQUESTACIÓN DE HILOS (Reemplazo de fun main())
    // ----------------------------------------------------------------------
    fun startThreads() {
        // Hilo 1: El productor de cronómetros
        thread(name = "Hilo Creador") {
            while (true) {
                val comando = colaCreacion.take()
                println("[Hilo Creador] Creando cronómetro '${comando.nombre}'...")

                // Creación del cronómetro (con un color por defecto simple)
                val nuevoCrono = Cronometro(comando.nombre)
                cronometros.putIfAbsent(comando.nombre, nuevoCrono)

                // Actualiza el StateFlow para notificar a la UI
                _cronometrosState.value = cronometros.toMap()
            }
        }

        // Hilo 2: El gestor de cronómetros
        thread(name = "Hilo Gestor") {
            while (true) {
                val comando = colaGestion.take()
                when (comando.accion) {
                    is Accion.Iniciar -> cronometros[comando.nombre]?.iniciar()
                    is Accion.Detener -> cronometros[comando.nombre]?.detener()
                    is Accion.Estado -> {
                        // El estado se obtiene directamente del StateFlow por la UI,
                        // pero podríamos usar esto para enviar un mensaje de diagnóstico.
                    }
                }
            }
        }

        // NOTA: El "Hilo UI" de lectura de consola y el "Hilo de Visualización"
        // son reemplazados completamente por la magia de Jetpack Compose.
    }
}