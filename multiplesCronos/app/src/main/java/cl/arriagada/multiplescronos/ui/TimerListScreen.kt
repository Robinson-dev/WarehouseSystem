package cl.arriagada.multiplescronos.ui

//import android.graphics.drawable.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material.icons.Icons // Contenedor principal de íconos
import androidx.compose.material.icons.filled.Add // El ícono "Add" específico

//import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cl.arriagada.multiplescronos.model.Cronometro

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
//import androidx.test.espresso.base.Default


import kotlin.collections.toList



val DarkBlueBackground = Color(0xFF1F2937) // Fondo general oscuro
val DarkCardColor = Color(0xFF374151) // Color de las tarjetas de cronómetro
val ButtonColor = Color(0xFF3B82F6) // Color primario de botones

@Composable
fun TimerListItem(
    crono: Cronometro,
    onControlClick: (String) -> Unit // Acción al presionar el botón de control
) {
    // Escuchar el estado del cronómetro
    val tiempo by crono.tiempoTranscurrido.collectAsState()
    val isRunning by crono.estaCorriendo.collectAsState()

    // Formatear el tiempo usando la función de la clase Cronometro
    val formattedTime = crono.tiempoFormateado()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { /* Navegar a la vista de detalle TODO */ },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCardColor)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = crono.nombre, // Nombre del cronómetro (Pizza, Pasta)
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formattedTime.substring(3), // Mostrar solo Minutos:Segundos
                    color = ButtonColor, // Color de acento para el tiempo
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Light
                )
            }

            // Botón de Pausa/Play (como el círculo de la imagen)
            Button(
                onClick = { onControlClick(crono.nombre) },
                colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.size(48.dp),
                // Añadimos contentPadding para centrar mejor el ícono/texto
                contentPadding = PaddingValues(0.dp)
            ) {
                // El ícono de "Play" se ve mejor como un triángulo que como ">"
                val buttonText = if (isRunning) "II" else "▶"
                Text(text = buttonText, color = Color.White, fontSize = 18.sp)
            }
        }
    }
}

/**
 * Vista principal de la lista de cronómetros.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerListScreen(
    cronometros: Map<String, Cronometro>, // Recibe el mapa de cronómetros
    onAddNewTimer: () -> Unit,
    onControlTimer: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Timers", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBlueBackground),
                actions = {
                    IconButton(onClick = onAddNewTimer) {
                        Icon(
                            imageVector = Icons.Default.Add, // <-- USA 'imageVector =' y 'Icons' (en plural)
                            contentDescription = "Add Timer",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        containerColor = DarkBlueBackground // Aplicar color de fondo
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(cronometros.values.toList()) { crono ->
                TimerListItem(crono = crono, onControlClick = onControlTimer)
            }
        }
    }
}