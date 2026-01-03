package cl.arriagada.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import cl.arriagada.todoapp.ui.theme.TodoAppTheme

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainPage(modifier = Modifier.padding(innerPadding))
                    val todoName = remember {
                        mutableStateOf("")
                    }
                    // obtiene el context de android para acceder al sistema de archivos
                    val context = LocalContext.current
                    // lee todas las tareas guardadas del archivo
                    val itemList = readData(context)
                    // row organiza los elementos en horizontal: textfield a la izquierda, boton a la derecha
                    Row(
                        modifier = Modifier
                            // fillmaxwidth hace que el row ocupe todo el ancho de la pantalla
                            .fillMaxWidth()
                            // padding agrega espacio alrededor del row
                            .padding(16.dp)
                    ) {
                        // textfield es el campo donde el usuario escribe la tarea
                        TextField(
                            // value es el texto actual que muestra el textfield
                            value = todoName.value,
                            // onvaluechange se ejecuta cada vez que el usuario escribe una letra
                            onValueChange = { todoName.value = it },
                            // label es el texto gris que aparece arriba cuando escribes
                            label = { Text(text = "ingresa tu tarea") },
                            // weight 1f hace que el textfield ocupe todo el espacio disponible
                            modifier = Modifier.weight(1f)
                        )

                        // spacer crea un espacio vacio de 8dp entre el textfield y el boton
                        Spacer(modifier = Modifier.width(8.dp))

                        // boton para agregar la tarea a la lista
                        Button(
                            onClick = {
                                // verifica que el texto no este vacio
                                if (todoName.value.isNotEmpty()) {
                                    // agrega la tarea a la lista
                                    itemList.add(todoName.value)
                                    // guarda la lista en el archivo
                                    writeData(itemList, context)
                                    // borra el texto del textfield
                                    todoName.value = ""
                                }
                            },
                            modifier = Modifier.height(56.dp)
                        ) {
                            Text(text = "agregar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainPage(modifier: Modifier = Modifier){

}

