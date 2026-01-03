package cl.arriagada.learningxcloude.polimorfismo

class Empleado(
        nombre: String,
        edad : Int,
    val salario: Int,
    val puesto : String
): Persona(nombre, edad) {
    override fun presentarse(): String {
        return super.presentarse() + ", soy ${puesto} y gano ${salario}"
    }
}
class Estudiante(
    nombre: String,
    edad: Int,
    val Matricula: String,
    val carrera: String
): Persona(nombre, edad){
    override fun presentarse(): String {
        return super.presentarse() + ", soy ${nombre} y estudio ${carrera}"
    }
}