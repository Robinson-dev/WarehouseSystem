package cl.arriagada.todoapp

import java.util.UUID

// Este es nuestro plano para una única tarea.
data class Todo(
    val id: UUID = UUID.randomUUID(), // Un identificador único para cada tarea.
    val task: String,                  // El texto de la tarea, ej: "Pasear al perro".
    val isCompleted: Boolean = false   // Para saber si ya la terminamos.
)
