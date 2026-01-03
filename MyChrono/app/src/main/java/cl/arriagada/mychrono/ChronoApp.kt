package cl.arriagada.mychrono

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import androidx.lifecycle.viewmodel.compose.viewModel

data class Chrono(val id: String = UUID.randomUUID().toString(), val name: String, var startTime: Long = 0, var elapsedTime: Long = 0, var isRunning: Boolean = false)

class ChronoViewModel : ViewModel() {
    private val _chronos = MutableStateFlow<List<Chrono>>(emptyList())
    val chronos: StateFlow<List<Chrono>> = _chronos.asStateFlow()

    init {
        viewModelScope.launch {
            while (true) {
                if (_chronos.value.any { it.isRunning }) {
                    val now = System.currentTimeMillis()
                    _chronos.value = _chronos.value.map {
                        if (it.isRunning) {
                            it.copy(elapsedTime = now - it.startTime)
                        } else {
                            it
                        }
                    }
                }
                delay(100)
            }
        }
    }

    fun addChrono(name: String) {
        val newChrono = Chrono(name = name)
        _chronos.value = _chronos.value + newChrono
    }

    fun toggleChrono(id: String) {
        val now = System.currentTimeMillis()
        _chronos.value = _chronos.value.map {
            if (it.id == id) {
                if (it.isRunning) {
                    it.copy(isRunning = false)
                } else {
                    it.copy(isRunning = true, startTime = now - it.elapsedTime)
                }
            } else {
                it
            }
        }
    }

    fun resetChrono(id: String) {
        _chronos.value = _chronos.value.map {
            if (it.id == id) {
                it.copy(isRunning = false, elapsedTime = 0)
            } else {
                it
            }
        }
    }
}

@Composable
fun ChronoApp(chronoViewModel: ChronoViewModel = viewModel()) {
    val chronos by chronoViewModel.chronos.collectAsState()
    var nextChronoNumber by remember { mutableIntStateOf(1) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Cronómetros Múltiples") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                chronoViewModel.addChrono("Chrono $nextChronoNumber")
                nextChronoNumber++
            }) {
                Text("+", fontSize = 24.sp) // Simple text for FAB
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            items(chronos, key = { it.id }) {
                ChronoItem(chrono = it, onToggle = { chronoViewModel.toggleChrono(it.id) }, onReset = { chronoViewModel.resetChrono(it.id) })
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ChronoItem(chrono: Chrono, onToggle: () -> Unit, onReset: () -> Unit) {
    val hours = chrono.elapsedTime / 3600000
    val minutes = (chrono.elapsedTime % 3600000) / 60000
    val seconds = (chrono.elapsedTime % 60000) / 1000
    val formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(chrono.name, style = MaterialTheme.typography.titleMedium)
            Text(formattedTime, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            Row {
                Button(onClick = onToggle) {
                    Text(if (chrono.isRunning) "Pausa" else "Inicio")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onReset, enabled = !chrono.isRunning && chrono.elapsedTime > 0) {
                    Text("Reset")
                }
            }
        }
    }
}