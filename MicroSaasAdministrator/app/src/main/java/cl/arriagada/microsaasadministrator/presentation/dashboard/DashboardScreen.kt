package cl.arriagada.microsaasadministrator.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.room.jarjarred.org.antlr.v4.codegen.model.Sync

/**
 * Dashboard Screen - Primera pantalla funcional de la aplicación.
 *
 * Esta es la pantalla principal que muestra:
 * - Bodega activa (con selector)
 * - Estadísticas: productos, stock bajo, entradas hoy
 * - Botón de sincronización
 * - Bottom navigation (placeholder)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    // Colecta estados del ViewModel
    val selectedWarehouse by viewModel.selectedWarehouse.collectAsState()
    val warehouses by viewModel.warehouses.collectAsState()
    val totalProducts by viewModel.totalProducts.collectAsState()
    val lowStockCount by viewModel.lowStockCount.collectAsState()
    val entriesCountToday by viewModel.entriesCountToday.collectAsState()
    val isSyncing by viewModel.isSyncing.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val lastSyncTime by viewModel.lastSyncTime.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Estado local para dropdown de bodegas
    var showWarehouseDropdown by remember { mutableStateOf(false) }

    // Si está cargando, muestra spinner
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    // Pantalla principal
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // ============== TOP APP BAR ==============
        TopAppBar(
            title = {
                Text(
                    text = "INVENTORY",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = { /* TODO: Abrir menú lateral */ }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }
            },
            actions = {
                // Avatar placeholder
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFF1976D2), shape = RoundedCornerShape(50))
                        .clickable { /* TODO: Abrir perfil */ },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "JD",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        // ============== CONTENIDO PRINCIPAL ==============
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Selector de bodega
            item {
                Column {
                    Text(
                        text = "BODEGA ACTUAL",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF9E9E9E)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Dropdown de bodegas
                    Box {
                        Button(
                            onClick = { showWarehouseDropdown = !showWarehouseDropdown },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White
                            )
                        ) {
                            Text(
                                text = selectedWarehouse?.name ?: "Seleccionar bodega",
                                color = Color(0xFF1976D2),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Start
                            )
                            Text(
                                text = "▼",
                                color = Color(0xFF1976D2)
                            )
                        }

                        DropdownMenu(
                            expanded = showWarehouseDropdown,
                            onDismissRequest = { showWarehouseDropdown = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            warehouses.forEach { warehouse ->
                                DropdownMenuItem(
                                    text = {
                                        Text(warehouse.name)
                                    },
                                    onClick = {
                                        viewModel.selectWarehouse(warehouse)
                                        showWarehouseDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Alerta de stock bajo
            item {
                if (lowStockCount > 0) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFF3E0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "⚠️",
                                fontSize = 24.sp,
                                modifier = Modifier.padding(end = 12.dp)
                            )

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "$lowStockCount productos con stock bajo",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Requiere atención inmediata",
                                    fontSize = 12.sp,
                                    color = Color(0xFF9E9E9E)
                                )
                            }

                            Text(
                                text = ">",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1976D2)
                            )
                        }
                    }
                }
            }

            // Tarjetas de estadísticas
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Tarjeta: Productos
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = totalProducts.toString(),
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Productos",
                                fontSize = 12.sp,
                                color = Color(0xFF9E9E9E)
                            )
                        }
                    }

                    // Tarjeta: Stock Bajo
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = lowStockCount.toString(),
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFD32F2F)
                            )
                            Text(
                                text = "Stock Bajo",
                                fontSize = 12.sp,
                                color = Color(0xFF9E9E9E)
                            )
                        }
                    }

                    // Tarjeta: Entradas Hoy
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = entriesCountToday.toString(),
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF6200EE)
                            )
                            Text(
                                text = "Entradas Hoy",
                                fontSize = 12.sp,
                                color = Color(0xFF9E9E9E)
                            )
                        }
                    }
                }
            }

            // Información de sincronización
            item {
                Column {
                    Text(
                        text = "Última sincronización: ${viewModel.getLastSyncTimeFormatted()}",
                        fontSize = 12.sp,
                        color = Color(0xFF9E9E9E),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Botón de sincronización
                    Button(
                        onClick = { viewModel.triggerManualSync() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1976D2)
                        ),
                        enabled = !isSyncing
                    ) {
                        if (isSyncing) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(end = 8.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Text("Sincronizando...")
                        } else {
                            Icon(
                                Icons.Default.Sync,
                                contentDescription = "Sincronizar",
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(end = 8.dp)
                            )
                            Text("SINCRONIZAR AHORA")
                        }
                    }
                }
            }
        }

        // ============== BOTTOM NAVIGATION ==============
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(icon = "🏠", label = "HOME", isActive = true)
            NavItem(icon = "📦", label = "PRODUCTOS")
            NavItem(icon = "📝", label = "MOVIMIENTOS")
            NavItem(icon = "👤", label = "PERFIL")
        }
    }
}

/**
 * Componente para item de navegación inferior
 */
@Composable
private fun NavItem(
    icon: String,
    label: String,
    isActive: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { /* TODO: Navegar */ }
            .padding(8.dp)
    ) {
        Text(
            text = icon,
            fontSize = 20.sp
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = if (isActive) Color(0xFF1976D2) else Color(0xFF9E9E9E)
        )
    }
}