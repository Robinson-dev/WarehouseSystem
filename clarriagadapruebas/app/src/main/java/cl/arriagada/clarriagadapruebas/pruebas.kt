package cl.arriagada.clarriagadapruebas

fun main(){


 //list()
// sentences()


}

fun sentences(){
    println("Sentencias if y when")
    println("ingresa tu nombre")
    val name = readln()
    if (true){
        print(name)
    }else{
        print("no se encontró un nombre")
    }
}


fun list(){
    println("Listas y sus Metodos")
    val names = listOf("ari","sari","vichito","robin",)

    println(names)

    names.forEach {
        println(it)
    }

    val name = readln()
    val search = names.find { it == name}
    println(search)

    val searchAdvance = names.find { it.equals(name, true) }
    println(searchAdvance)
}