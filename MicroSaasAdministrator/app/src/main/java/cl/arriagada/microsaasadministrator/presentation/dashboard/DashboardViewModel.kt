package cl.arriagada.microsaasadministrator.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.arriagada.microsaasadministrator.dao.CompanyDao
import cl.arriagada.microsaasadministrator.dao.MovementDao
import cl.arriagada.microsaasadministrator.dao.ProductDao
import cl.arriagada.microsaasadministrator.dao.WarehouseDao
import cl.arriagada.microsaasadministrator.data.local.entity.WarehouseEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel del Dashboard - Pantalla principal de la aplicación.
 *
 * Responsabilidades:
 * - Cargar datos de la empresa y bodega activa
 * - Mostrar estadísticas: productos, stock bajo, entradas hoy
 * - Permitir cambio de bodega
 * - Disparar sincronización manual
 *
 * @param companyDao DAO de empresas
 * @param warehouseDao DAO de bodegas
 * @param productDao DAO de productos
 * @param movementDao DAO de movimientos
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val companyDao: CompanyDao,
    private val warehouseDao: WarehouseDao,
    private val productDao: ProductDao,
    private val movementDao: MovementDao
) : ViewModel() {

    // ============== ESTADOS ==============

    /**
     * Empresa activa del usuario
     * null = no hay empresa cargada
     */
    private val _currentCompanyId = MutableStateFlow<String?>(null)
    val currentCompanyId: StateFlow<String?> = _currentCompanyId.asStateFlow()

    /**
     * Bodega actualmente seleccionada
     * null = no hay bodega cargada
     */
    private val _selectedWarehouse = MutableStateFlow<WarehouseEntity?>(null)
    val selectedWarehouse: StateFlow<WarehouseEntity?> = _selectedWarehouse.asStateFlow()

    /**
     * Lista de todas las bodegas de la empresa
     */
    private val _warehouses = MutableStateFlow<List<WarehouseEntity>>(emptyList())
    val warehouses: StateFlow<List<WarehouseEntity>> = _warehouses.asStateFlow()

    /**
     * Cantidad total de productos en bodega activa
     */
    private val _totalProducts = MutableStateFlow(0)
    val totalProducts: StateFlow<Int> = _totalProducts.asStateFlow()

    /**
     * Cantidad de productos con stock bajo
     */
    private val _lowStockCount = MutableStateFlow(0)
    val lowStockCount: StateFlow<Int> = _lowStockCount.asStateFlow()

    /**
     * Cantidad de entradas registradas hoy
     */
    private val _entriesCountToday = MutableStateFlow(0)
    val entriesCountToday: StateFlow<Int> = _entriesCountToday.asStateFlow()

    /**
     * Última vez que se sincronizó
     * null = nunca se sincronizó
     */
    private val _lastSyncTime = MutableStateFlow<Long?>(null)
    val lastSyncTime: StateFlow<Long?> = _lastSyncTime.asStateFlow()

    /**
     * Indicador si está sincronizando
     */
    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    /**
     * Indicador de error general
     */
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    /**
     * Indicador si los datos están cargados
     */
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // ============== INICIALIZACIÓN ==============

    init {
        Timber.d("DashboardViewModel inicializado")
        loadDashboard()
    }

    // ============== FUNCIONES PÚBLICAS ==============

    /**
     * Carga todos los datos del dashboard
     *
     * Flujo:
     * 1. Obtiene bodega activa de la empresa
     * 2. Carga lista de bodegas
     * 3. Carga estadísticas
     * 4. Restaura última hora de sincronización
     */
    fun loadDashboard() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                Timber.d("Cargando dashboard...")

                // TODO: Obtener company_id de la sesión de usuario
                // Por ahora, usamos un valor de prueba
                val companyId = "default-company-id"
                _currentCompanyId.value = companyId

                // Carga bodegas de la empresa
                warehouseDao.getActiveWarehousesByCompany(companyId).collect { warehouses ->
                    _warehouses.value = warehouses
                    Timber.d("Bodegas cargadas: ${warehouses.size}")

                    // Selecciona la bodega por defecto
                    if (warehouses.isNotEmpty() && _selectedWarehouse.value == null) {
                        selectWarehouse(warehouses[0])
                    }
                }

            } catch (e: Exception) {
                Timber.e("Error cargando dashboard: ${e.message}")
                _errorMessage.value = "Error al cargar datos: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Selecciona una bodega y carga sus estadísticas
     *
     * @param warehouse Bodega a seleccionar
     */
    fun selectWarehouse(warehouse: WarehouseEntity) {
        viewModelScope.launch {
            try {
                Timber.d("Seleccionando bodega: ${warehouse.name}")
                _selectedWarehouse.value = warehouse

                // Carga estadísticas de la bodega
                loadWarehouseStats(warehouse.id)

            } catch (e: Exception) {
                Timber.e("Error seleccionando bodega: ${e.message}")
                _errorMessage.value = "Error al cambiar bodega"
            }
        }
    }

    /**
     * Dispara sincronización manual con Supabase
     */
    fun triggerManualSync() {
        viewModelScope.launch {
            try {
                _isSyncing.value = true
                Timber.d("Iniciando sincronización manual...")

                // TODO: Implementar sincronización real con Supabase
                // Por ahora solo simula un delay
                kotlinx.coroutines.delay(2000)

                _lastSyncTime.value = System.currentTimeMillis()
                Timber.d("Sincronización completada")

            } catch (e: Exception) {
                Timber.e("Error en sincronización: ${e.message}")
                _errorMessage.value = "Error en sincronización: ${e.message}"
            } finally {
                _isSyncing.value = false
            }
        }
    }

    /**
     * Limpia mensaje de error
     */
    fun clearError() {
        _errorMessage.value = null
    }

    // ============== FUNCIONES PRIVADAS ==============

    /**
     * Carga estadísticas de una bodega específica
     *
     * @param warehouseId ID de la bodega
     */
    private suspend fun loadWarehouseStats(warehouseId: String) {
        try {
            // Cuenta total de productos
            productDao.getActiveProductCount(warehouseId).collect { count ->
                _totalProducts.value = count
                Timber.d("Total de productos: $count")
            }

            // TODO: Contar productos con stock bajo
            // Por ahora es hardcoded
            _lowStockCount.value = 0

            // TODO: Contar movimientos de hoy
            // Por ahora es hardcoded
            _entriesCountToday.value = 0

        } catch (e: Exception) {
            Timber.e("Error cargando estadísticas: ${e.message}")
        }
    }

    /**
     * Formatea tiempo desde última sincronización
     * Ejemplo: "hace 2 min", "hace 5 seg", "hace 1 hora"
     */
    fun getLastSyncTimeFormatted(): String {
        val syncTime = _lastSyncTime.value ?: return "Nunca"
        val now = System.currentTimeMillis()
        val diffMs = now - syncTime

        return when {
            diffMs < 60 * 1000 -> "hace ${diffMs / 1000} seg"
            diffMs < 60 * 60 * 1000 -> "hace ${diffMs / (60 * 1000)} min"
            diffMs < 24 * 60 * 60 * 1000 -> "hace ${diffMs / (60 * 60 * 1000)} horas"
            else -> "Sincronizado"
        }
    }
}

/**
 * Data class para representar el estado del dashboard
 */
data class DashboardState(
    val isLoading: Boolean = true,
    val selectedWarehouse: WarehouseEntity? = null,
    val warehouses: List<WarehouseEntity> = emptyList(),
    val totalProducts: Int = 0,
    val lowStockCount: Int = 0,
    val entriesCountToday: Int = 0,
    val lastSyncTime: Long? = null,
    val isSyncing: Boolean = false,
    val errorMessage: String? = null
)