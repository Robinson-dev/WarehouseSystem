package com.example.myappcafe.Bebidas

// abstract significa que esta clase es como un molde general
// nunca vamos a crear una bebida generica, siempre sera un cafe o frappe especifico
abstract class Bebida(
    val nombre: String,             // toda bebida tiene nombre
    val precioBase: Double,         // toda bebida tiene precio base
    val tiempoPreparacion: Int = 4  // por defecto 4 segundos
) {
    /* 'abstract fun' es como un contrato o promesa
    // estamos diciendo toda bebida DEBE saber calcular su precio, pero no voy a decir como"
    // es como la funcion hablar() en humanos: todos los humanos pueden hablar
    // pero un mexicano habla espanol, un americano ingles, un chino mandarin
    // la funcion hablar() existe en todos, pero cada uno la implementa diferente */
    abstract fun calcularPrecio(tamano: String): Double

    // este metodo es comun para todas las bebidas
    fun obtenerPrecioTamano(tamano: String): Double {
        // when es como el switch de java pero mas poderoso
        return when (tamano.lowercase()) {
            "tall" -> 0.0      // tall es el tamano base, no cobra extra
            "grande" -> 700.0   // grande cuesta 700 mas
            "venti" -> 1200.0   // venti cuesta 1200 mas
            else -> 0.0         // si pone algo raro, no cobramos extra
        }
    }
}

// aqui es donde aplicamos herencia
// el ': Bebida' despues del nombre significa cafe hereda de cebida
// es como decir cafe ES UNA bebida
// cuando heredas, no solo recibes los atributos (nombre, precioBase)
// tambien heredas los metodos que luego VAS a implementar con tu propia logica
// Cafe recibe todo de Bebida y DEBE implementar calcularPrecio() a su manera
class Cafe(
    nombre: String,         // nota que aqui NO ponemos 'val' porque lo pasamos al padre
    precioBase: Double, val tipoGrano: String,  // este SI tiene val porque es propio de Cafe
    val intensidad: Int     // este tambien es exclusivo de Cafe
) : Bebida(nombre, precioBase) {  // aqui le pasamos los parametros al constructor padre
    // 'override' significa que estamos sobrescribiendo el metodo abstracto del padre
    override fun calcularPrecio(tamano: String): Double {
        var precio = precioBase
        // el arabica es mas caro
        if (tipoGrano.lowercase() == "arabica") {
            precio += 500
        }
        precio += obtenerPrecioTamano(tamano)
        return precio
    }
}

// segunda clase hija
class Frappuccino(
    nombre: String, precioBase: Double, val tieneCream: Boolean,  // boolean = true o false
    val sabor: String
) : Bebida(nombre, precioBase) {
    override fun calcularPrecio(tamano: String): Double {
        var precio = precioBase
        if (tieneCream) {
            precio += 800  // la crema es cara
        }
        // listOf crea una lista temporal solo para verificar si el sabor esta ahi
        if (sabor.lowercase() in listOf("caramelo", "mocha")) {
            precio += 300  // sabores premium
        }
        precio += obtenerPrecioTamano(tamano)
        return precio
    }
}