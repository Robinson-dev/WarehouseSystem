package cl.arriagada.foodexpress.models
import java.text.NumberFormat
import java.util.Locale
abstract class Producto(
    val nombre: String,
    val precioBase: Double,
    val categoria: String,
    val tiempoPreparacionSeg: Int = 1
) {
    init {
        if (precioBase < 0) {
            throw IllegalArgumentException("El precio base no puede ser negativo.")
        }
    }
    abstract fun calcularPrecioFinal(): Double // se usara double porque luego los calculos de iva,
    // y de tipo de cliente puede dar eventualmente un numero decimal

    open fun obtenerDescripcion(): String {
        val formatoMoneda = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
        return "$nombre - ${formatoMoneda.format(calcularPrecioFinal())}"
    }
}
class Comida(
    nombre: String,
    precioBase: Double,
    val esPremium: Boolean,
    tiempoPreparacionSeg: Int
) : Producto(nombre, precioBase, "Comida", tiempoPreparacionSeg) {


    override fun calcularPrecioFinal(): Double {
        return if (esPremium) precioBase * 1.20 else precioBase
    }


    override fun obtenerDescripcion(): String {
        val descripcionBase = super.obtenerDescripcion()
        return if (esPremium) "$descripcionBase (Premium)" else descripcionBase
    }
}


class Bebida(
    nombre: String,
    precioBase: Double,
    val tamano: String,
    tiempoPreparacionSeg: Int = 0
) : Producto(nombre, precioBase, "Bebida", tiempoPreparacionSeg) {

    override fun calcularPrecioFinal(): Double {
        return when (tamano.lowercase()) {
            "pequeño" -> precioBase * 0.90
            "mediano" -> precioBase * 1.15
            "grande" -> precioBase * 1.30
            else -> precioBase
        }
    }
    override fun obtenerDescripcion(): String {
        val descripcionBase = super.obtenerDescripcion()
        return "$descripcionBase (${tamano.lowercase()})"
    }
}