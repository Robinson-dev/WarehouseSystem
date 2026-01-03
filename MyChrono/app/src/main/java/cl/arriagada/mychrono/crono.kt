package cl.arriagada.mychrono

import kotlin.system.exitProcess
import java.lang.Thread
import kotlin.concurrent.thread

fun cronoBasico(){
    println("--- Cronometro de cocina ---")
    println("- Presiona 'Enter' para detener -")

    val startTime = System.currentTimeMillis() // guarda el tiempo de inicio

    thread {
        readln() // espera a que el usuario poreciones Enter
        exitProcess(0)// termina la app cuando se preciona Enter
    }

    //Bucle principal del cronometro

    while (true){
        val tiempoTranscurrido=(System.currentTimeMillis() - startTime)
        val horas = tiempoTranscurrido / 3600000 //
        val minutos = (tiempoTranscurrido % 3600000) / 60000 //
        val segundos = (tiempoTranscurrido % 60000) / 1000 //

        val tiempoFormateado = String.format("%02d:%02d:%02d", horas, minutos, segundos)

        print("\rTiempo transcurrido:  $tiempoFormateado")

        Thread.sleep(1000)//pausa el hilo por 1 seg
    }
}

fun main(){
    cronoBasico()
}