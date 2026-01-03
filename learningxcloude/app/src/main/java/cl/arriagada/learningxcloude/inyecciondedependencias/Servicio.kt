package cl.arriagada.learningxcloude.inyecciondedependencias

class Servicio(private val bd: BaseDatos) {
    fun procesarYGuardar(datos: String){
        val procesados = datos.uppercase()
        bd.guardar(procesados)
    }

    fun recuperarDatos(): String{
        return bd.obtener()
    }
}