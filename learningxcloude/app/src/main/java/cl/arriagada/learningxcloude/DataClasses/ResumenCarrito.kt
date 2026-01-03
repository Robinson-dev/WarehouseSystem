package cl.arriagada.learningxcloude.DataClasses

data class ResumenCarrito(
    val totalProductos: Int,
    val precioTotal: Double,
    val items: List<String>
)