package com.example.myapplication.poo

open class Person(open val firstName: String, open val lastName: String) {

    open fun showActivity()="${firstName} esta tomando algo"
}
//la Herencia se aplica con OPEN adelante de la clase o del metodo o propiedad ¡¡
// y se implementa con un override en el metodo y esto permite
// implementarlo con distintos argumentos

class tipoClass(){
    val propiedad_1: Int=2;
    val propiedad_2: String="nombre";
    val esPropiedad_3: Boolean= true;

    fun mostrarAlgo(){
        println("Esta funcion muestra un numero: $propiedad_1 , un nombre: $propiedad_2 y si es válido algo $esPropiedad_3 ")
    }
}