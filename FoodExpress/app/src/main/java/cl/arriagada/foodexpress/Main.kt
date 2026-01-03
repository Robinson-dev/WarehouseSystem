package cl.arriagada.foodexpress
import cl.arriagada.foodexpress.models.EstadoPedido
import cl.arriagada.foodexpress.models.Producto
import cl.arriagada.foodexpress.services.GestorPedidos
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    println("=== BIENVENIDO A FOODEXPRESS ===")
    GestorPedidos.mostrarCatalogo()
    val pedidoActual = seleccionarProductos()
    if (pedidoActual.isEmpty()) {
        println("No se seleccionaron productos. Adios.")
        return@runBlocking
    }
    val tipoCliente = solicitarTipoCliente()

    println("\nIniciando procesamiento de su pedido...")
    GestorPedidos.procesarPedido(pedidoActual) { estado ->
        when (estado) {
            is EstadoPedido.EnPreparacion -> println("Estado: En Preparacion...")
            is EstadoPedido.ListoParaEntrega -> println("Estado final: ¡Pedido Listo para entregar!")
            is EstadoPedido.Error -> println("Estado: Error - ${estado.mensaje}")
            is EstadoPedido.Pendiente -> println("Estado: Pedido pendiente...")
        }
    }
    GestorPedidos.generarResumen(pedidoActual, tipoCliente)
}

private fun seleccionarProductos(): List<Producto> {
    val pedido = mutableListOf<Producto>()
    while (true) {
        try {
            print("Seleccione productos (ej: 1,1,3 etc. o 'fin' para terminar): ")
            val input = readlnOrNull() ?: "fin"

            if (input.equals("fin", ignoreCase = true)) {
                break
            }

            val indicesSeleccionados = input.split(',')
                .map { it.trim().toInt() - 1 }

            for (indice in indicesSeleccionados) {
                if (indice in GestorPedidos.catalogo.indices) {
                    val productoSeleccionado = GestorPedidos.catalogo[indice]
                    pedido.add(productoSeleccionado)
                    println(" > '${productoSeleccionado.nombre}' añadido al pedido.")
                } else {
                    println("  Numero de producto invalido: ${indice + 1}")
                }
            }
            break
        } catch (e: NumberFormatException) {
            println("Error: Por favor, ingrese solo numeros separados por comas o 'fin'.")
        } catch (e: Exception) {
            println("Ocurrio un error inesperado: ${e.message}")
        }
    }
    return pedido
}
private fun solicitarTipoCliente(): String {
    val tiposValidos = setOf("regular", "vip", "premium")
    while (true) {
        print("Ingrese tipo de cliente (regular/vip/premium): ")
        val tipo = readlnOrNull()?.lowercase() ?: ""
        if (tipo in tiposValidos) {
            return tipo
        } else {
            println("Tipo de cliente no valido. Por favor, intente de nuevo.")
        }
    }
}