package com.example.myappcafe.GestorOrdenes
import com.example.myappcafe.EstadoOrden.EstadoOrden
import com.example.myappcafe.Bebidas.Bebida
import com.example.myappcafe.Bebidas.Cafe
import com.example.myappcafe.Bebidas.Frappuccino
import kotlinx.coroutines.delay
class GestorOrdenes {
    // el menu con todas las bebidas
    private val menu = listOf(
        Cafe("Esspresso", 2500.0, "Arabica", 5),
        Cafe("Americano", 3200.0, "Robusta", 3),
        Cafe("Latte", 4500.0, "Arabica", 2),
        Cafe("Cappuccino", 4200.0, "Robusta", 3),
        Frappuccino("Frapp Caramelo", 5500.0, true, "Caramelo"),
        Frappuccino("Frapp Mocha", 5800.0, true, "Mocha"),
        Frappuccino("Frapp Vainilla", 4900.0, false, "Vainilla")
    )
////////////////////////////////////////////////////////////////////////////
    fun mostrarMenu() {
        println("\n=== SISTEMA STARBREW ===\n")
        println("Menu disponible:")
        menu.forEachIndexed { index, bebida ->
            // when es como el switch de java pero mas poderoso
            // IMPORTANTE: el when necesita else para que kotlin este feliz
            val precio = when (bebida) {
                is Cafe -> {
                    bebida.precioBase.toInt() + if (bebida.tipoGrano == "Arabica") 500 else 0
                }
                is Frappuccino -> {
                    bebida.precioBase.toInt() + if (bebida.tieneCream) 800 else 0
                }
                else -> bebida.precioBase.toInt()  // siempre pon else aunque no se use
            }
            val extra = when (bebida) {
                is Cafe -> "(" + bebida.tipoGrano + ")"
                is Frappuccino -> if (bebida.tieneCream) "(con crema)" else ""
                else -> ""  // otro else necesario
            }
            // mostramos cada bebida numerada
            val linea = (index + 1).toString() + ". " + bebida.nombre + " " + extra + " - $" + precio
            println(linea)
        }
    }
//////////////////////////////////////////////////////////////
    fun obtenerBebida(indice: Int):Bebida? {
        return if (indice in 1..menu.size) {
            menu[indice - 1]  // -1 porque las listas empiezan en 0
        } else {
            null  // si pone un numero invalido
        }
    }
/////////////////Obtener-Descuento///////////////////////////////////////////////
    fun calcularDescuento(tipoCliente: String): Double {
        return when(tipoCliente.lowercase()) {  // when = switch de java
            "regular" -> 0.0
            "miembro" -> 0.10
            "gold" -> 0.15
            "partner" -> 0.30
            else -> 0.0
        }
    }

    /////////////////////////////////////////////////////////////////
    // 'suspend' permite usar delay sin bloquear el programa
    suspend fun procesarOrden(bebida: Bebida, tamano: String): EstadoOrden {
        println("\nProcesando orden...")
        println("Estado: Preparando bebida...")
        delay(4000)  // Espera 4 Segundos
        return EstadoOrden.Lista
    }
    /////////////////////////////////////////////////////////////
    fun generarResumen(
        bebida: Bebida,
        tamano: String,
        tipoCliente: String,
        estadoFinal: EstadoOrden
    ) {
        println("\n=== RESUMEN DE LA ORDEN ===")
        val precioFinal = bebida.calcularPrecio(tamano)
        //// mostramos diferente info segun el tipo de bebida
        when(bebida) {  // when = switch de java
            is Cafe -> {
                println("- " + bebida.nombre + " (" + bebida.tipoGrano + ") " + tamano + ": $" + precioFinal.toInt())
                println("  Precio base: $" + bebida.precioBase.toInt())
                if (bebida.tipoGrano == "Arabica") {
                    println("  Grano Arabica: +500")
                }
            }
            is Frappuccino -> {
                println("- " + bebida.nombre + " " + tamano + ": $" + precioFinal.toInt())
                println("  Precio base: $" + bebida.precioBase.toInt())
                if (bebida.tieneCream) {
                    println("  Crema batida: +800")
                }
            }
            else -> {
                println("- " + bebida.nombre + " " + tamano + ": $" + precioFinal.toInt())
            }
        }
        if (tamano != "tall") {
            println("  Tamano " + tamano + ": +" + bebida.obtenerPrecioTamano(tamano).toInt())
        }
//////////////////////////////////////////////////////////////////////
        // calculamos descuentos e impuestos

        val descuento = calcularDescuento(tipoCliente)
        val montoDescuento = precioFinal * descuento
        val subtotalConDescuento = precioFinal - montoDescuento
        val iva = subtotalConDescuento * 0.19
        val total = subtotalConDescuento + iva
        println("\nSubtotal: $" + precioFinal.toInt())
        if (descuento > 0) {
            val porcentaje = (descuento * 100).toInt()
            println("Descuento " + tipoCliente + " (" + porcentaje + "%): -$" + montoDescuento.toInt())
            println("Subtotal con descuento: $" + subtotalConDescuento.toInt())
        }
        println("IVA (19%): $" + iva.toInt())
        println("TOTAL: $" + total.toInt())
        println("\nEstado final: Lista")
    }
}