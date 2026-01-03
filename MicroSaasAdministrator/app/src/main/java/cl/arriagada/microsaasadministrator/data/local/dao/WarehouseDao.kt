package cl.arriagada.microsaasadministrator.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cl.arriagada.microsaasadministrator.data.local.entity.WarehouseEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones con WarehouseEntity.
 * Maneja todas las operaciones CRUD de bodegas.
 */
@Dao
interface WarehouseDao {

    /**
     * Inserta una bodega nueva
     *
     * @param warehouse Entidad de bodega
     * @return ID de la fila insertada
     */
    @Insert
    suspend fun insertWarehouse(warehouse: WarehouseEntity): Long

    /**
     * Actualiza una bodega existente
     *
     * @param warehouse Entidad actualizada
     */
    @Update
    suspend fun updateWarehouse(warehouse: WarehouseEntity)

    /**
     * Obtiene una bodega por su ID
     *
     * @param warehouseId ID único de la bodega
     * @return Flow que emite la bodega
     */
    @Query("SELECT * FROM warehouses WHERE id = :warehouseId")
    fun getWarehouseById(warehouseId: String): Flow<WarehouseEntity?>

    /**
     * Obtiene todas las bodegas de una empresa
     *
     * @param companyId ID de la empresa
     * @return Flow que emite lista de bodegas
     */
    @Query("SELECT * FROM warehouses WHERE company_id = :companyId ORDER BY name ASC")
    fun getWarehousesByCompany(companyId: String): Flow<List<WarehouseEntity>>

    /**
     * Obtiene todas las bodegas activas de una empresa
     *
     * @param companyId ID de la empresa
     * @return Flow que emite bodegas activas
     */
    @Query("""
        SELECT * FROM warehouses 
        WHERE company_id = :companyId AND isActive = 1 
        ORDER BY name ASC
    """)
    fun getActiveWarehousesByCompany(companyId: String): Flow<List<WarehouseEntity>>

    /**
     * Obtiene la bodega principal de una empresa
     * (se crea automáticamente con nombre "Bodega Principal")
     *
     * @param companyId ID de la empresa
     * @return Flow que emite la bodega principal
     */
    @Query("""
        SELECT * FROM warehouses 
        WHERE company_id = :companyId AND name = 'Bodega Principal' 
        LIMIT 1
    """)
    fun getDefaultWarehouse(companyId: String): Flow<WarehouseEntity?>

    /**
     * Obtiene una bodega por nombre dentro de una empresa
     *
     * @param companyId ID de la empresa
     * @param name Nombre de la bodega
     * @return Flow que emite la bodega
     */
    @Query("""
        SELECT * FROM warehouses 
        WHERE company_id = :companyId AND name = :name 
        LIMIT 1
    """)
    fun getWarehouseByName(companyId: String, name: String): Flow<WarehouseEntity?>

    /**
     * Cuenta bodegas activas de una empresa
     *
     * @param companyId ID de la empresa
     * @return Flow que emite el count
     */
    @Query("""
        SELECT COUNT(*) FROM warehouses 
        WHERE company_id = :companyId AND isActive = 1
    """)
    fun getActiveWarehouseCount(companyId: String): Flow<Int>

    /**
     * Obtiene bodegas no sincronizadas
     *
     * @return Flow que emite bodegas pendientes de sync
     */
    @Query("SELECT * FROM warehouses WHERE is_synced = 0 ORDER BY createdAt ASC")
    fun getUnsyncedWarehouses(): Flow<List<WarehouseEntity>>

    /**
     * Marca una bodega como sincronizada
     *
     * @param warehouseId ID de la bodega
     */
    @Query("UPDATE warehouses SET is_synced = 1, updatedAt = :timestamp WHERE id = :warehouseId")
    suspend fun markAsSynced(warehouseId: String, timestamp: Long = System.currentTimeMillis())

    /**
     * Verifica si existe una bodega con cierto nombre en una empresa
     *
     * @param companyId ID de la empresa
     * @param name Nombre a buscar
     * @return true si existe
     */
    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM warehouses 
            WHERE company_id = :companyId AND name = :name
        )
    """)
    suspend fun warehouseNameExists(companyId: String, name: String): Boolean

    /**
     * Desactiva una bodega (soft delete)
     * No borra datos, solo marca como inactiva
     *
     * @param warehouseId ID de la bodega
     */
    @Query("UPDATE warehouses SET isActive = 0, updatedAt = :timestamp WHERE id = :warehouseId")
    suspend fun deactivateWarehouse(warehouseId: String, timestamp: Long = System.currentTimeMillis())

    /**
     * Elimina una bodega (hard delete)
     * CUIDADO: Elimina en cascada productos y movimientos
     *
     * @param warehouse Entidad a eliminar
     */
    @Delete
    suspend fun deleteWarehouse(warehouse: WarehouseEntity)

    /**
     * Limpia todas las bodegas (para testing)
     */
    @Query("DELETE FROM warehouses")
    suspend fun deleteAllWarehouses()
}