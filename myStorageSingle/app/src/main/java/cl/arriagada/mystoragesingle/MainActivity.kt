package cl.arriagada.mystoragesingle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cl.arriagada.mystoragesingle.ui.theme.MyStorageSingleTheme
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable


val supabase= createSupabaseClient(
    supabaseUrl = "https://erigndwzfmqfvqowocdm.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImVyaWduZHd6Zm1xZnZxb3dvY2RtIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjY5NjUwNjAsImV4cCI6MjA4MjU0MTA2MH0.eSHq6B-nktmtbpvUSGw3fV4T9C9bB2u-U3k3AGWt_-k"
) {
    install(Postgrest)
    //install other modules
}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyStorageSingleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    ProductosList()
                }

            }
        }
    }
}

@Serializable
data class Producto(
    val id: Int,
    val nombre_producto: String,
    val cantidad: Int,
    val unidadMedida : String
)

@Composable
fun ProductosList(){

    val Productos = remember { mutableStateListOf<Producto>() }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO){
            val  result= supabase.from("bodega").select().decodeList<Producto>()
            Productos.addAll(result)
        }
    }
    LazyColumn {
        items(Productos){
            Producto ->
            ListItem(headlineContent = {Text(text = Producto.nombre_producto)})
        }
    }
}