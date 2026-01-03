package com.example.myappcafe.Main

import com.example.myappcafe.GestorOrdenes.GestorOrdenes
import kotlinx.coroutines.*

//runBlocking es necesario para usar funciones suspend
fun main() = runBlocking {
    val gestor = GestorOrdenes()
    try {
        gestor.mostrarMenu()
        print("\nSeleccione bebida (numero): ")
        val opcionBebida = readLine()?.toIntOrNull() ?: 0
        val bebida = gestor.obtenerBebida(opcionBebida)
        if (bebida == null) {
            println("Opcion invalida!")
            return@runBlocking
        }
        println("\nSeleccione tamano:")
        println("1. Tall (12 oz) - Precio base")
        println("2. Grande (16 oz) - +$700")
        println("3. Venti (20 oz) - +$1,200")
        print("Seleccion: ")
        val opcionTamano = readLine()?.toIntOrNull() ?: 0
        val tamano = when (opcionTamano) {  // when = switch de java
            1 -> "tall"
            2 -> "grande"
            3 -> "venti"
            else -> {
                println("Tamano invalido! Usando Tall")
                "tall"
            }
        }
        print("\n¿Desea agregar otra bebida? (s/n): ")
        val respuesta = readLine()?.lowercase() ?: "n"
        if (respuesta == "s") {
            println("Por ahora solo procesamos una bebida")
        }
        print("\nTipo de cliente (regular/miembro/gold/partner): ")
        val tipoCliente = readLine()?.lowercase() ?: "regular"
        // procesamos la orden (tarda 4 segundos)
        val estadoFinal = gestor.procesarOrden(bebida, tamano)
        // mostramos el resumen
        gestor.generarResumen(bebida, tamano, tipoCliente, estadoFinal)
        println("\n☕ Gracias por usar StarBrew!")
    } catch (e: Exception) {
        println("Error: ${e.message}")
        println("Por favor intente nuevamente")
    }
}