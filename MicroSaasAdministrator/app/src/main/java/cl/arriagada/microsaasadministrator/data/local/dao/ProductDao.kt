package cl.arriagada.microsaasadministrator.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cl.arriagada.microsaasadministrator.data.local.dao.ProductEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones con ProductEntity.
 *
 * IMPORTANTE: Esta entidad NO contiene stock.
 * El stock se calcula desde MovementEntity.
 */
@Dao
interface ProductDao {

    /**
     * Inserta un producto nuevo
     *
     * @param product Entidad de producto
     * @return ID de la fila insertada
     */
    @Insert
    suspend fun insertProduct(product: ProductEntity): Long

    /**
     * Actualiza un producto existente
     *
     * @param product Entidad actualizada
     */
    @Update
    suspend fun updateProduct(product: ProductEntity)

    /**
     * Obtiene un producto por su ID
     *
     * @param productId ID único del producto
     * @return Flow que emite el producto
     */
    @Query("SELECT * FROM products WHERE id = :productId")
    fun getProductById(productId: String): Flow<ProductEntity?>

    /**
     * Obtiene todos los productos de una bodega
     * Ordenados por nombre
     *
     * @param warehouseId ID de la bodega
     * @return Flow que emite lista de productos
     */
    @Query("""
        SELECT * FROM products 
        WHERE warehouse_id = :warehouseId 
        ORDER BY name ASC
    """)
    fun getProductsByWarehouse(warehouseId: String): Flow<List<ProductEntity>>

    /**
     * Obtiene productos activos de una bodega
     *
     * @param warehouseId ID de la bodega
     * @return Flow que emite productos activos
     */
    @Query("""
        SELECT * FROM products 
        WHERE warehouse_id = :warehouseId AND isActive = 1 
        ORDER BY name ASC
    """)
    fun getActiveProductsByWarehouse(warehouseId: String): Flow<List<ProductEntity>>

    /**
     * Busca productos por nombre en una bodega (búsqueda parcial)
     *
     * @param warehouseId ID de la bodega
     * @param searchQuery Texto a buscar (case-insensitive)
     * @return Flow que emite productos coincidentes
     */
    @Query("""
        SELECT * FROM products 
        WHERE warehouse_id = :warehouseId 
        AND name LIKE '%' || :searchQuery || '%' 
        AND isActive = 1
        ORDER BY name ASC
    """)
    fun searchProductsByName(warehouseId: String, searchQuery: String): Flow<List<ProductEntity>>

    /**
     * Obtiene todos los productos de una empresa
     * (útil para exportación o reportes)
     *
     * @param companyId ID de la empresa
     * @return Flow que emite todos los productos
     */
    @Query("""
        SELECT * FROM products 
        WHERE company_id = :companyId 
        ORDER BY name ASC
    """)
    fun getProductsByCompany(companyId: String): Flow<List<ProductEntity>>

    /**
     * Obtiene un producto por nombre en una bodega específica
     *
     * @param warehouseId ID de la bodega
     * @param name Nombre del producto
     * @return Flow que emite el producto
     */
    @Query("""
        SELECT * FROM products 
        WHERE warehouse_id = :warehouseId AND name = :name 
        LIMIT 1
    """)
    fun getProductByName(warehouseId: String, name: String): Flow<ProductEntity?>

    /**
     * Cuenta productos activos en una bodega
     *
     * @param warehouseId ID de la bodega
     * @return Flow que emite el count
     */
    @Query("""
        SELECT COUNT(*) FROM products 
        WHERE warehouse_id = :warehouseId AND isActive = 1
    """)
    fun getActiveProductCount(warehouseId: String): Flow<Int>

    /**
     * Obtiene productos no sincronizados
     *
     * @return Flow que emite productos pendientes de sync
     */
    @Query("SELECT * FROM products WHERE is_synced = 0 ORDER BY createdAt ASC")
    fun getUnsyncedProducts(): Flow<List<ProductEntity>>

    /**
     * Marca un producto como sincronizado
     *
     * @param productId ID del producto
     */
    @Query("UPDATE products SET is_synced = 1, updatedAt = :timestamp WHERE id = :productId")
    suspend fun markAsSynced(productId: String, timestamp: Long = System.currentTimeMillis())

    /**
     * Verifica si existe un producto con cierto nombre en una bodega
     *
     * @param warehouseId ID de la bodega
     * @param name Nombre a buscar
     * @return true si existe
     */
    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM products 
            WHERE warehouse_id = :warehouseId AND name = :name AND isActive = 1
        )
    """)
    suspend fun productNameExists(warehouseId: String, name: String): Boolean

    /**
     * Obtiene productos con stock bajo en una bodega
     * (stock actual < min_stock)
     *
     * NOTA: El cálculo del stock actual debe hacerse en la aplicación
     * sumando los movimientos
     *
     * @param warehouseId ID de la bodega
     * @return Flow que emite productos con stock bajo
     */
    @Query("""
        SELECT * FROM products 
        WHERE warehouse_id = :warehouseId 
        AND isActive = 1
        ORDER BY name ASC
    """)
    fun getLowStockProducts(warehouseId: String): Flow<List<ProductEntity>>

    /**
     * Desactiva un producto (soft delete)
     *
     * @param productId ID del producto
     */
    @Query("UPDATE products SET isActive = 0, updatedAt = :timestamp WHERE id = :productId")
    suspend fun deactivateProduct(productId: String, timestamp: Long = System.currentTimeMillis())

    /**
     * Elimina un producto (hard delete)
     * CUIDADO: Elimina en cascada sus movimientos
     *
     * @param product Entidad a eliminar
     */
    @Delete
    suspend fun deleteProduct(product: ProductEntity)

    /**
     * Limpia todos los productos
     */
    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()
}