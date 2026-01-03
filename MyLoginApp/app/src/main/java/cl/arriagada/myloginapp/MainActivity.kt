package cl.arriagada.myloginapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.arriagada.myloginapp.ui.theme.MyLoginAppTheme
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

// ==============================
// CLIENTE SUPABASE
// ==============================

val supabase = createSupabaseClient(
    supabaseUrl = "https://jcnlxbcztvatrgqubriw.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Impjbmx4YmN6dHZhdHJncXVicml3Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjUwNTcyODAsImV4cCI6MjA4MDYzMzI4MH0.S20xeOhSPXrAvIJkr-HYhe90aziR9igKbO_-r0MTMoQ"
) {
    install(Postgrest)
}

// ==============================
// MODELOS
// ==============================

@Serializable
data class Usuarios(
    val id: Int,
    val nombre: String,
    val telefono: Long,
    val mail: String,
    val created_at: String? = null
)

@Serializable
data class NuevoUsuario(
    val nombre: String,
    val telefono: Long,
    val mail: String
)

// ==============================
// MAIN ACTIVITY
// ==============================

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyLoginAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { padding ->
                    UserList(modifier = Modifier.padding(padding))
                }
            }
        }
    }
}

// ==============================
// UI + LÓGICA
// ==============================

@Composable
fun UserList(modifier: Modifier = Modifier) {
    val usuarios = remember { mutableStateListOf<Usuarios>() }
    val scope = rememberCoroutineScope()

    // ---------- Cargar usuarios ----------
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                val result = supabase.from("usuarios").select().decodeList<Usuarios>()
                usuarios.addAll(result)
                println("✅ Usuarios cargados: ${result.size}")
            } catch (e: Exception) {
                println("❌ ERROR al cargar usuarios: ${e.message}")
            }
        }
    }

    // ---------- UI ----------
    Column(modifier = modifier.fillMaxSize()) {

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(usuarios) { usuario ->
                ListItem(
                    headlineContent = { Text(usuario.nombre) },
                    supportingContent = {
                        Text("Teléfono: ${usuario.telefono} | Email: ${usuario.mail}")
                    }
                )
            }
        }

        // FORMULARIO
        var newNombre by remember { mutableStateOf("") }
        var newTelefono by remember { mutableStateOf("") }
        var newMail by remember { mutableStateOf("") }

        Column(modifier = Modifier.padding(16.dp)) {

            OutlinedTextField(
                value = newNombre,
                onValueChange = { newNombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = newTelefono,
                onValueChange = { newTelefono = it },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = newMail,
                onValueChange = { newMail = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            Button(
                onClick = {
                    scope.launch(Dispatchers.IO) {

                        if (newNombre.isBlank() || newTelefono.isBlank() || newMail.isBlank()) {
                            println("⚠️ Todos los campos son obligatorios")
                            return@launch
                        }

                        val telefonoLong = newTelefono.toLongOrNull()
                        if (telefonoLong == null) {
                            println("⚠️ Teléfono inválido")
                            return@launch
                        }

                        try {
                            val usuarioInsertado = supabase.from("usuarios").insert(
                                NuevoUsuario(
                                    nombre = newNombre,
                                    telefono = telefonoLong,
                                    mail = newMail
                                )
                            ) {
                                select()
                                single()
                            }.decodeAs<Usuarios>()

                            usuarios.add(usuarioInsertado)

                            newNombre = ""
                            newTelefono = ""
                            newMail = ""

                            println("✅ Usuario guardado correctamente")

                        } catch (e: Exception) {
                            println("❌ ERROR al guardar usuario: ${e.message}")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Usuario")
            }
        }
    }
}
