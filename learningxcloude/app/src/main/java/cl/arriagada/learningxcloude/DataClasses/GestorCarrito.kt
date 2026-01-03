package cl.arriagada.learningxcloude.DataClasses

class GestorCarrito {
    fun agregarProducto(carrito: Carrito, producto: Producto){
        carrito.productos.add(producto)
        println("${producto.nombre} agregado al carrito")
    }

    fun  calcularTotal(carrito:Carrito): Double{
        return carrito.productos.sumOf {it.precio * it.cantidad }
    }

    fun generarResumen(carrito:Carrito): ResumenCarrito{
        return  ResumenCarrito(
            totalProductos = carrito.productos.size,
            precioTotal = calcularTotal(carrito),
            items = carrito.productos.map { it.nombre }
        )
    }
}
