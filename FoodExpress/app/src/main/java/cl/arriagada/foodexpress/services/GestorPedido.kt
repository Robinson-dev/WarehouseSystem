package cl.arriagada.foodexpress.services

import cl.arriagada.foodexpress.models.Bebida
import cl.arriagada.foodexpress.models.Comida
import cl.arriagada.foodexpress.models.EstadoPedido
import cl.arriagada.foodexpress.models.Producto
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Locale

object GestorPedidos {
    private val formatoMoneda = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    private const val IVA = 0.19

    val catalogo: List<Producto> = listOf(
        Comida("Hamburguesa Clasica", 8990.0, esPremium = false, tiempoPreparacionSeg = 5),
        Comida("Salmon Grillado", 15990.0, esPremium = true, tiempoPreparacionSeg = 8),
        Bebida("Coca Cola", 1990.0, tamano = "mediano"),
        Bebida("Jugo Natural", 2990.0, tamano = "grande")
    )
    fun mostrarCatalogo() {
        println("---- Catalogo Disponible ----")

        catalogo.forEachIndexed { index, producto ->
            println("${index + 1}. ${producto.obtenerDescripcion()}")
        }
        println("---------------------------")
    }

    private fun calcularSubtotal(pedido: List<Producto>): Double {
        return pedido.sumOf { it.calcularPrecioFinal() }
    }
    private fun calcularDescuento(subtotal: Double, tipoCliente: String): Double {
        val porcentajeDescuento = when (tipoCliente.lowercase()) {
            "regular" -> 0.05
            "vip" -> 0.10
            "premium" -> 0.15
            else -> 0.0
        }
        return subtotal * porcentajeDescuento
    }

    private fun calcularImpuestos(montoBase: Double): Double {
        return montoBase * IVA
    }

    suspend fun procesarPedido(pedido: List<Producto>, onEstadoChange: (EstadoPedido) -> Unit) {
        try {
            onEstadoChange(EstadoPedido.Pendiente)
            println("Procesando pedido...")
            onEstadoChange(EstadoPedido.EnPreparacion)

            val tiempoTotalPreparacion = pedido.sumOf {it.tiempoPreparacionSeg }
            delay(tiempoTotalPreparacion * 1000L)

            onEstadoChange(EstadoPedido.ListoParaEntrega)
        } catch (e: Exception) {
            onEstadoChange(EstadoPedido.Error("Hubo un problema en la cocina: ${e.message}"))
        }
    }

    fun generarResumen(pedido: List<Producto>, tipoCliente: String) {
        val subtotal = calcularSubtotal(pedido)
        val descuento = calcularDescuento(subtotal, tipoCliente)
        val baseImponible = subtotal - descuento
        val impuestos = calcularImpuestos(baseImponible)
        val total = baseImponible + impuestos

        println("\n=== RESUMEN DEL PEDIDO ===")
        pedido.forEach {
            println("- ${it.nombre}: ${formatoMoneda.format(it.calcularPrecioFinal())}")
        }
        println("---------------------------")
        println("Subtotal: ${formatoMoneda.format(subtotal)}")
        println("Descuento (${tipoCliente.lowercase()}): -${formatoMoneda.format(descuento)}")
        println("IVA (19%): ${formatoMoneda.format(impuestos)}")
        println("---------------------------")
        println("TOTAL A PAGAR: ${formatoMoneda.format(total)}")
        println("===========================")
    }
}