package cl.arriagada.multiplescronos.model

import android.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread


class Cronometro(
    val nombre: String,
    val color: Int = Color.LTGRAY) {

    // ----------------------------------------------------------------------
    // PROPIEDADES OBSERVABLES PARA LA UI (Jetpack Compose)
    // ----------------------------------------------------------------------
    private val _tiempoTranscurrido = MutableStateFlow(0L)
            val tiempoTranscurrido = _tiempoTranscurrido.asStateFlow() // Expone el valor como StateFlow

    private val _estaCorriendo = MutableStateFlow(false)
            val estaCorriendo = _estaCorriendo.asStateFlow()

    private var tiempoInicial: Long = 0L
    private var hilo: Thread? = null


    fun iniciar() {
        if (_estaCorriendo.value) return // Usa .value para StateFlow

        _estaCorriendo.value = true
        tiempoInicial = System.currentTimeMillis()

        hilo = thread {
            while (_estaCorriendo.value) { // Usa .value para StateFlow
                val transcurrido = System.currentTimeMillis() - tiempoInicial
                _tiempoTranscurrido.value = transcurrido // Actualiza el StateFlow
                Thread.sleep(1000)
            }
        }
    }

    fun detener() {
        _estaCorriendo.value = false
        // Lógica adicional para el futuro: hilo?.interrupt() si fuera necesario.
    }

    fun tiempoFormateado(): String {
        val transcurrido = _tiempoTranscurrido.value
        val horas = transcurrido / 3600000
        val minutos = (transcurrido % 3600000) / 60000
        val segundos = (transcurrido % 60000) / 1000
        return String.format("%02d:%02d:%02d", horas, minutos, segundos)
    }
}


