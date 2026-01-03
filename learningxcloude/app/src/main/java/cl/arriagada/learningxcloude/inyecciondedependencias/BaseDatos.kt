package cl.arriagada.learningxcloude.inyecciondedependencias

interface BaseDatos {
    fun guardar(datos: String)
    fun obtener(): String
}

class BaseDatosSQL: BaseDatos{
    private var datos= ""

    override fun guardar(datos: String) {
        this.datos = datos
        println("Datos guardados enn SQL: ${datos}")
    }

    override fun obtener(): String {
return datos
    }
}