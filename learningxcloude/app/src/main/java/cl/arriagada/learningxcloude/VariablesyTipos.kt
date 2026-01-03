package cl.arriagada.learningxcloude

////val = inmutable (usa esto por defecto)
//
//val nombre: String = "Juan"
//val edad: Int = 25
//val altura: Double = 1.75
//val EsActivo: Boolean = true
//
////var mutable (solo cuando NECESITES cambiar el valor)
//
//var ciudad = "santiago" //string inferido
//var precio =19.99 //Double inferido
//
//var contador: Int = 0
//contador = 1
//contador+=1
//
////Null safety (concepto clave en Kotlin)
//
//var apellido: String = "Pérez"
////apellido = null // error: no acepta null
//
//var apellidoOpcional: String? = "Perez"
//    apellidoOpcional = null

fun main(){
    val nombre: String? = "Ana"
    val edad: Int = 28
    var email: String? = null
    var saldo: Double? = 1500.50

    println("usuario: $nombre, edad: $edad")
    println("Email: $email")
    println("Saldo: $saldo")
    println(sumar(2,9))
    println(multiplicar(6,5))
    println(saludar("Robinson"))
    println(crearUsuario(null, null,null))

    println(calcular(10.0, 5.0))                    // 15.0
    println(calcular(10.0, 5.0, "resta"))           // 5.0
    println(calcular(10.0, 5.0, "multiplicacion"))  // 50.0

}
//fun con retorno explicito
fun sumar(a: Int, b: Int): Int{
    return a + b
}
//fun de expresion, de una sola linea
fun multiplicar(a: Int, b: Int): Int = a * b

//funcion sin retorno Unit es como void en java
fun saludar(nombre: String){
    println("Hola $nombre")
}

//parametros por defecto
fun crearUsuario(nombre: String?, edad: Int?=18, activo: Boolean? = true){
    println("Usuario: $nombre, $edad años, activo $activo ")
}

//funcion de calculadora

fun calcular(a: Double, b: Double, operacion: String="suma"): Double{
    return when(operacion){
        "suma"-> a + b
        "resta"-> a - b
        "multiplicacion" -> a * b
        "divsion" -> if (b!= 0.0) a / b else 0.0
        else -> 0.0
    }
}

//trabajando con null safety

