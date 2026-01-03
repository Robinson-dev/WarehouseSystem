package cl.arriagada.learningxcloude.DataClasses

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val cantidad: Int
)

data class Carrito(
    val id: Int,
    val productos: MutableList<Producto> = mutableListOf()
)