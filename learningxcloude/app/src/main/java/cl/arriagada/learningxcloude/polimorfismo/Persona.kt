package cl.arriagada.learningxcloude.polimorfismo

abstract class Persona(
    open val nombre: String,
    open val edad: Int
) {
    open fun presentarse(): String{
        return "Hola, Soy ${nombre} y tengo ${edad}"
    }
}