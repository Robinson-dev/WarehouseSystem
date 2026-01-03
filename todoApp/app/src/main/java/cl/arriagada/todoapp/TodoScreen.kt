package cl.arriagada.todoapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun TodoScreen(todoViewModel: TodoViewModel = viewModel()) {

    val todoList by todoViewModel.todos.collectAsState()
    var text by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mi Lista de Tareas") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // --- Zona de CREACIÓN (Create) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Nueva tarea") },
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = {
                    if (text.isNotBlank()) {
                        todoViewModel.addTask(text)
                        text = "" // Limpiar el campo de texto
                    }
                }) {
                    Text("Añadir")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Zona de LECTURA (Read) ---
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Ahora, para cada 'todo', LLAMAMOS al componente TodoItem
                items(todoList, key = { it.id }) { todo ->
                    TodoItem( // <-- PASO 2: LLAMADA AL COMPONENTE
                        todo = todo,
                        onCompletedToggle = { todoViewModel.toggleCompleted(todo.id) },
                        onDelete = { todoViewModel.deleteTask(todo.id) }
                    )
                }
            }
        }
    }
}

// PASO 1: Mover la definición de TodoItem aquí, fuera de TodoScreen
@Composable
fun TodoItem(
    todo: Todo,
    onCompletedToggle: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = todo.isCompleted,
                onCheckedChange = { onCompletedToggle() }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = todo.task,
                modifier = Modifier.weight(1f),
                // Si la tarea está completa, la tachamos
                textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else null
            )
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar tarea")
            }
        }
    }
}
