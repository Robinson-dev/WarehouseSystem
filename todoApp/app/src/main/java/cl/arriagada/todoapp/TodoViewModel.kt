package cl.arriagada.todoapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class TodoViewModel : ViewModel() {

    // Este es el "estado" privado y mutable. Solo el ViewModel puede cambiarlo.
    // Es como el borrador del director de orquesta.
    private val _todos = MutableStateFlow<List<Todo>>(emptyList())

    // Este es el estado público e inmutable. La UI solo puede leerlo.
    // Es la partitura final que ven los músicos.
    val todos: StateFlow<List<Todo>> = _todos.asStateFlow()

    // --- OPERACIONES CRUD ---

    // CREATE (Crear)
    fun addTask(taskText: String) {
        val newTodo = Todo(task = taskText)
        _todos.value = _todos.value + newTodo
    }

    // UPDATE (Actualizar)
    fun toggleCompleted(id: UUID) {
        _todos.update { currentList ->
            currentList.map { todo ->
                if (todo.id == id) {
                    todo.copy(isCompleted = !todo.isCompleted)
                } else {
                    todo
                }
            }
        }
    }

    // DELETE (Borrar)
    fun deleteTask(id: UUID) {
        _todos.update { currentList ->
            currentList.filterNot { it.id == id }
        }
    }
}