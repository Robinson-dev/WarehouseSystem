package cl.arriagada.learningxcloude.ejemplousuarios

import cl.arriagada.learningxcloude.ejemplousuarios.Usuario

class RepositorioUsuarios {
    private val usuarios = mutableListOf<Usuario>()

    fun agregarUsuario(usuario: Usuario){
        usuarios.add(usuario)
        println("Usuario agregado : ${usuario.nombre}" )
    }

    fun obtenerUsuarioPorId(id: Int): Usuario?{
        return  usuarios.find { it.id == id }
    }

    fun obtenerTodos(): List<Usuario>{
        return usuarios
    }

    fun borrarUsuario(id: Int){
        usuarios.removeAt(id)
    }

    fun borrarTodos(id: Int){
        return usuarios.clear()
    }
}