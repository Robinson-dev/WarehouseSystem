package cl.arriagada.learningxcloude

import cl.arriagada.learningxcloude.DataClasses.Carrito
import cl.arriagada.learningxcloude.DataClasses.GestorCarrito
import cl.arriagada.learningxcloude.DataClasses.Producto
import cl.arriagada.learningxcloude.Factorypattern.FabricaNotificaciones
import cl.arriagada.learningxcloude.ejemplousuarios.RepositorioUsuarios
import cl.arriagada.learningxcloude.ejemplousuarios.Usuario
import cl.arriagada.learningxcloude.inyecciondedependencias.BaseDatosSQL
import cl.arriagada.learningxcloude.inyecciondedependencias.Servicio
import cl.arriagada.learningxcloude.polimorfismo.Empleado
import cl.arriagada.learningxcloude.polimorfismo.Estudiante
import cl.arriagada.learningxcloude.polimorfismo.Persona

fun main(){
    println("=== EJEMPLO 1: Composición ===")
    val repo = RepositorioUsuarios()
    val user1 = Usuario(1, "Juan", "juan@email.com")
    val user2 = Usuario(2, "María", "maria@email.com")
    repo.agregarUsuario(user1)
    repo.agregarUsuario(user2)
    println("Usuario encontrado: ${repo.obtenerUsuarioPorId(1)}\n")

    println("=== EJEMPLO 2: Herencia y Polimorfismo ===")
    val empleado: Persona = Empleado("Carlos", 30, 50000, "Ingeniero")
    val estudiante: Persona = Estudiante("Ana", 22, "2024001", "Ingeniería de Software")
    println(empleado.presentarse())
    println(estudiante.presentarse())
    println()

    println("=== EJEMPLO 3: Inyección de Dependencias ===")
    val bd = BaseDatosSQL()
    val servicio = Servicio(bd)
    servicio.procesarYGuardar("datos importantes")
    println("Datos recuperados: ${servicio.recuperarDatos()}\n")

    println("=== EJEMPLO 4: Data Classes ===")
    val carrito = Carrito(123)
    val gestor = GestorCarrito()
    gestor.agregarProducto(carrito, Producto(1, "Laptop", 999.99, 1))
    gestor.agregarProducto(carrito, Producto(2, "Mouse", 29.99, 2))
    println("Total del carrito: $${gestor.calcularTotal(carrito)}")
    val resumen = gestor.generarResumen(carrito)
    println("Resumen: $resumen\n")

    println("=== EJEMPLO 5: Factory Pattern ===")
    val fabrica = FabricaNotificaciones()
    val notif1 = fabrica.crearNotificacionesEmail("usuario@email.com", "Bienvenido!")
    val notif2 = fabrica.crearNotificacionSMS("+34612345678", "Tu código es 1234")
    println(notif1.enviar())
    println(notif2.enviar())

}