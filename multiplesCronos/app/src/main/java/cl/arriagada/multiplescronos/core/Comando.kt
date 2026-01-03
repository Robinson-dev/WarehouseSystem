package cl.arriagada.multiplescronos.core

sealed class Comando {
    data class Crear(val nombre: String) : Comando()
    data class Gestionar(val nombre: String, val accion: Accion) : Comando()
}

sealed class Accion {
    object Iniciar : Accion()
    object Detener : Accion()
    object Estado : Accion()
}