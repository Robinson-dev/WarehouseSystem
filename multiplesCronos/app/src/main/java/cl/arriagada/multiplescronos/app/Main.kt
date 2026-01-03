package cl.arriagada.multiplescronos.app

import cl.arriagada.multiplescronos.core.Accion
import cl.arriagada.multiplescronos.core.Comando
import cl.arriagada.multiplescronos.model.Cronometro
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread
import kotlin.system.exitProcess


fun main() {
    println("--- Cronometros ---")
    println("Crea y controla sus cronometros asignandoles un nombre")
    println("comandos: 'Iniciar: <nombre>', 'Detener <nombre>', 'Salir'")

    val colaCreacion = LinkedBlockingQueue<Comando.Crear>()
    val colaGestion = LinkedBlockingQueue<Comando.Gestionar>()
    val cronometros = ConcurrentHashMap<String, Cronometro>()

    val _cronometrosState = MutableStateFlow<Map<String, Cronometro>>(emptyMap())
    thread(name = "Hilo Creador") {
        while (true) {
            val comando = colaCreacion.take()
            val nuevoCrono = Cronometro(comando.nombre)
            cronometros.putIfAbsent(comando.nombre, nuevoCrono)

            // ¡Actualiza el StateFlow para notificar a la UI!
            _cronometrosState.value = cronometros.toMap()
        }
    }

    thread(name = "Hilo Gestor") {
        while (true) {
            val comando = colaGestion.take()
            when (comando.accion) {
                is Accion.Iniciar -> {
                    println("\n[Hilo Gestor] iniciando cronometro ${comando.nombre}...")
                    cronometros[comando.nombre]?.iniciar()
                }

                is Accion.Detener -> {
                    println("\n[Hilo Gestor] Deteniendo cronometro ${comando.nombre}...")
                    cronometros[comando.nombre]?.detener()
                }

                is Accion.Estado -> {
                    if (cronometros.isEmpty()) {
                        println("\n[Hilo Gestor] no hay cronometros creados")
                    } else {
                        println("\nn[Hilo Gestor] Estado de los cronómetros: ${cronometros.keys}")
                    }
                }
            }
        }

    }
    // Hilo 3: Interfaz con el usuario
    while (true) {
        print("> ")
        val entrada = readlnOrNull() ?: continue
        val partes = entrada.split(" ")
        val comandoStr = partes[0].lowercase()
        val nombre = partes.getOrNull(1)?.lowercase()

        when (comandoStr) {
            "crear" -> if (nombre != null) colaCreacion.put(Comando.Crear(nombre)) else println("Debes especificar un nombre.")
            "iniciar" -> if (nombre != null) colaGestion.put(
                Comando.Gestionar(
                    nombre, Accion.Iniciar
                )
            ) else println("Debes especificar un nombre")

            "detener" -> if (nombre != null) colaGestion.put(
                Comando.Gestionar(
                    nombre, Accion.Detener
                )
            ) else println("Debes especificar un nombre.")

            "estado" -> colaGestion.put(Comando.Gestionar("", Accion.Estado))
            "salir" -> {
                println("Saliendo del programa...")
                exitProcess(0)
            }

            else -> println("Comando no reconocido.")
        }
    }

}
