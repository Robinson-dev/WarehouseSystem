

open class Animal(val nombre: String, val edad: Int){
    open fun hacerSonido(){
        println("El animal hace un sonido")
    }
    fun dormir(){
        println("el $nombre está durmiendo")
    }
}

class Perro(nombre: String, edad: Int, val raza: String):Animal(nombre, edad){
    override fun hacerSonido() {
        println("Guau , Guau ¡¡")
    }
    fun traerPelota(){
        println("el perro $nombre, de raza $raza ha traido la pelota")
    }
}

class Gato(nombre: String, edad: Int):Animal(nombre, edad){
    override fun hacerSonido() {
        println("Miau ¡¡")
    }
    fun treparArbol(){
        println("el gato $nombre, esta trepando un Arbol")
    }
}

fun main(){

    val miPerro = Perro("Fido",5, "Beagle")
    val miGato = Gato("Garfield", 3)

    //metodos heredados
    miPerro.hacerSonido()
    miGato.hacerSonido()
    miPerro.dormir()
    //metodos propios de la clase hija
    miPerro.traerPelota()

    //Polimorfismo

    val animales: List<Animal> = listOf(miPerro, miGato)
    for (animal in animales){
        animal.hacerSonido()
    }

}

