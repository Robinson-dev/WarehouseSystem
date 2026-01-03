package cl.arriagada.microsaasadministrator.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import cl.arriagada.microsaasadministrator.data.local.entity.MovementEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones con MovementEntity.
 *
 * PRINCIPIO CLAVE:
 * "Los movimientos NO se editan, solo se insertan o eliminan en cascada"
 * Por eso NO hay método @Update.
 *
 * El stock actual se calcula como:
 * SUM(CASE WHEN type = 'ENTRADA' THEN quantity
 *         WHEN type = 'SALIDA' THEN -quantity
 *         WHEN type = 'AJUSTE' THEN quantity
 *    END)
 */
@Dao
interface MovementDao {

    /**
     * Inserta un movimiento nuevo
     *
     * @param movement Entidad de movimiento
     * @return ID de la fila insertada
     */
    @Insert
    suspend fun insertMovement(movement: MovementEntity): Long

    /**
     * Obtiene un movimiento por su ID
     *
     * @param movementId ID único del movimiento
     * @return Flow que emite el movimiento
     */
    @Query("SELECT * FROM movements WHERE id = :movementId")
    fun getMovementById(movementId: String): Flow<MovementEntity?>

    /**
     * Obtiene todos los movimientos de un producto en una bodega
     * Ordenados por fecha/hora más reciente
     *
     * @param productId ID del producto
     * @param warehouseId ID de la bodega
     * @return Flow que emite lista de movimientos
     */
    @Query("""
        SELECT * FROM movements 
        WHERE product_id = :productId AND warehouse_id = :warehouseId
        ORDER BY date DESC, time DESC
    """)
    fun getMovementsByProduct(productId: String, warehouseId: String): Flow<List<MovementEntity>>

    /**
     * Obtiene todos los movimientos de una bodega
     *
     * @param warehouseId ID de la bodega
     * @return Flow que emite lista de movimientos
     */
    @Query("""
        SELECT * FROM movements 
        WHERE warehouse_id = :warehouseId
        ORDER BY date DESC, time DESC
    """)
    fun getMovementsByWarehouse(warehouseId: String): Flow<List<MovementEntity>>

    /**
     * Obtiene movimientos de una bodega filtrando por tipo
     *
     * @param warehouseId ID de la bodega
     * @param type Tipo de movimiento (ENTRADA, SALIDA, AJUSTE)
     * @return Flow que emite movimientos del tipo especificado
     */
    @Query("""
        SELECT * FROM movements 
        WHERE warehouse_id = :warehouseId AND type = :type
        ORDER BY date DESC, time DESC
    """)
    fun getMovementsByType(warehouseId: String, type: String): Flow<List<MovementEntity>>

    /**
     * Obtiene movimientos de una empresa
     * (útil para reportes globales)
     *
     * @param companyId ID de la empresa
     * @return Flow que emite lista de movimientos
     */
    @Query("""
        SELECT * FROM movements 
        WHERE company_id = :companyId
        ORDER BY date DESC, time DESC
    """)
    fun getMovementsByCompany(companyId: String): Flow<List<MovementEntity>>

    /**
     * Obtiene movimientos dentro de un rango de fechas
     *
     * @param warehouseId ID de la bodega
     * @param fromDate Fecha inicial (ISO format: YYYY-MM-DD)
     * @param toDate Fecha final (ISO format: YYYY-MM-DD)
     * @return Flow que emite movimientos en rango
     */
    @Query("""
        SELECT * FROM movements 
        WHERE warehouse_id = :warehouseId AND date BETWEEN :fromDate AND :toDate
        ORDER BY date DESC, time DESC
    """)
    fun getMovementsByDateRange(
        warehouseId: String,
        fromDate: String,
        toDate: String
    ): Flow<List<MovementEntity>>

    /**
     * Obtiene movimientos recientes de hoy
     *
     * @param warehouseId ID de la bodega
     * @param todayDate Fecha de hoy (ISO format: YYYY-MM-DD)
     * @return Flow que emite movimientos de hoy
     */
    @Query("""
        SELECT * FROM movements 
        WHERE warehouse_id = :warehouseId AND date = :todayDate
        ORDER BY time DESC
    """)
    fun getTodayMovements(warehouseId: String, todayDate: String): Flow<List<MovementEntity>>

    /**
     * CÁLCULO DE STOCK ACTUAL para un producto en una bodega
     *
     * Suma todos los movimientos respetando el tipo:
     * - ENTRADA: suma
     * - SALIDA: resta
     * - AJUSTE: suma (el signo está en la cantidad)
     *
     * @param productId ID del producto
     * @param warehouseId ID de la bodega
     * @return Flow que emite el stock actual (Double)
     */
    @Query("""
        SELECT COALESCE(SUM(
            CASE 
                WHEN type = 'ENTRADA' THEN quantity
                WHEN type = 'SALIDA' THEN -quantity
                WHEN type = 'AJUSTE' THEN quantity
                ELSE 0
            END
        ), 0.0)
        FROM movements
        WHERE product_id = :productId AND warehouse_id = :warehouseId
    """)
    fun getCurrentStock(productId: String, warehouseId: String): Flow<Double>

    /**
     * Obtiene movimientos no sincronizados
     * (para enviar a Supabase)
     *
     * @return Flow que emite movimientos pendientes
     */
    @Query("SELECT * FROM movements WHERE is_synced = 0 ORDER BY createdAt ASC")
    fun getUnsyncedMovements(): Flow<List<MovementEntity>>

    /**
     * Marca un movimiento como sincronizado
     *
     * @param movementId ID del movimiento
     */
    @Query("UPDATE movements SET is_synced = 1 WHERE id = :movementId")
    suspend fun markAsSynced(movementId: String)

    /**
     * Cuenta movimientos de un producto
     *
     * @param productId ID del producto
     * @param warehouseId ID de la bodega
     * @return Flow que emite el count
     */
    @Query("""
        SELECT COUNT(*) FROM movements 
        WHERE product_id = :productId AND warehouse_id = :warehouseId
    """)
    fun getMovementCount(productId: String, warehouseId: String): Flow<Int>

    /**
     * Obtiene el movimiento más reciente de un producto
     *
     * @param productId ID del producto
     * @param warehouseId ID de la bodega
     * @return Flow que emite el último movimiento
     */
    @Query("""
        SELECT * FROM movements 
        WHERE product_id = :productId AND warehouse_id = :warehouseId
        ORDER BY date DESC, time DESC
        LIMIT 1
    """)
    fun getLatestMovement(productId: String, warehouseId: String): Flow<MovementEntity?>

    /**
     * Elimina un movimiento (solo en cascada cuando se borra producto)
     * En operación normal, los movimientos son inmutables y no se eliminan
     *
     * @param movement Entidad a eliminar
     */
    @Delete
    suspend fun deleteMovement(movement: MovementEntity)

    /**
     * Limpia todos los movimientos
     * PELIGROSO: Solo para testing/reset
     */
    @Query("DELETE FROM movements")
    suspend fun deleteAllMovements()
}