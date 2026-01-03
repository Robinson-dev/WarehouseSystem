package cl.arriagada.microsaasadministrator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.arriagada.microsaasadministrator.presentation.dashboard.DashboardScreen
import cl.arriagada.microsaasadministrator.presentation.theme.InventoryTheme
import com.inventory.presentation.auth.login.LoginScreen
import com.russhwolf.settings.BuildConfig
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa Timber para logging en modo debug
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.d("🚀 Aplicación iniciada en modo DEBUG")
        }

        // Define el contenido de la UI con Compose
        setContent {
            InventoryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Configuración de navegación
                    val navController = rememberNavController()

                    // Gráfico de navegación
                    NavHost(
                        navController = navController,
                        // TODO: Cambiar a "login" para flujo de autenticación
                        // Por ahora comienza en Dashboard para verificar que funciona
                        startDestination = "dashboard"
                    ) {
                        // ============== Ruta de Login ==============
                        composable(route = "login") {
                            LoginScreen(
                                // Callback cuando login es exitoso
                                onLoginSuccess = { email ->
                                    Timber.d("✅ Login exitoso para: $email")
                                    // Navega al dashboard y elimina login del backstack
                                    navController.navigate("dashboard") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                // Callback para ir a signup
                                onNavigateToSignup = {
                                    Timber.d("Navegando a signup")
                                    navController.navigate("signup")
                                }
                            )
                        }

                        // ============== Ruta de Signup ==============
                        composable(route = "signup") {
                            // TODO: Implementar SignupScreen
                            Surface(color = MaterialTheme.colorScheme.background) {
                                // Placeholder
                            }
                        }

                        // ============== Ruta de Dashboard ==============
                        composable(route = "dashboard") {
                            DashboardScreen()
                        }

                        // ============== Rutas futuras ==============
                        // TODO: Productos
                        // TODO: Movimientos
                        // TODO: Bodegas
                        // TODO: Perfil
                    }
                }
            }
        }
    }
}


