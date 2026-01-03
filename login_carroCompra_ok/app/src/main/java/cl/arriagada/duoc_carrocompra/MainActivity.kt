package cl.arriagada.duoc_carrocompra

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.arriagada.duoc_carrocompra.ui.theme.Duoc_carroCompraTheme
import cl.arriagada.duoc_carrocompra.view.LoginScreen
import cl.arriagada.duoc_carrocompra.viewmodel.LoginViewModel
/**
 * Activity principal de la aplicación
 * Punto de entrada de la app
 *
 * ComponentActivity es la clase base para Activities que usan Jetpack Compose
 */
class MainActivity : ComponentActivity() {
    // onCreate se ejecuta cuando se crea la Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge() hace que la app use todo el espacio de la pantalla
        // Incluye las áreas de la barra de estado y navegación (edge-to-edge display)
        enableEdgeToEdge()

        // setContent define el contenido de la Activity usando Compose
        // Todo lo que está dentro es código declarativo de UI
        setContent {
            // CarroCompraTheme aplica el tema Material3 de la app
            // Define colores, tipografía, formas, etc.
            Duoc_carroCompraTheme {
                MainScreen()  // Pantalla principal
            }
        }
    }
}

/**
 * Pantalla principal que contiene el Scaffold y LoginScreen
 *
 * Scaffold proporciona la estructura básica de Material3
 * (barra superior, barra inferior, FAB, etc.)
 */
@Composable
fun MainScreen(
    // viewModel() crea o recupera el ViewModel
    // Sobrevive a cambios de configuración (rotación)
    viewModel: LoginViewModel = viewModel()
) {
    // LocalContext.current obtiene el Context actual
    // Necesario para Toast, navegación, acceso a recursos, etc.
    val context = LocalContext.current

    // Scaffold: estructura base de Material3
    // Maneja el padding para barras del sistema automáticamente
    Scaffold(
        modifier = Modifier.fillMaxSize()  // Ocupa toda la pantalla
    ) { innerPadding ->
        // innerPadding: padding calculado automáticamente por Scaffold
        // Evita que el contenido quede detrás de las barras del sistema

        LoginScreen(
            viewModel = viewModel,
            modifier = Modifier.padding(innerPadding),  // Aplicamos el innerPadding
            onLoginSuccess = {
                // Callback que se ejecuta cuando el login es exitoso
                // Aquí podrías navegar a otra pantalla con NavController

                // Toast: mensaje temporal que aparece en la pantalla
                Toast.makeText(
                    context,                          // Context de la app
                    "¡Login exitoso! Bienvenido",    // Mensaje a mostrar
                    Toast.LENGTH_LONG                 // Duración (LONG = ~3.5 segundos)
                ).show()                              // Muestra el Toast
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    Duoc_carroCompraTheme {
        MainScreen()
    }
}