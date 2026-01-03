package cl.arriagada.microsaasadministrator.data.local.dao


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import cl.arriagada.microsaasadministrator.data.local.entity.CompanyEntity
import cl.arriagada.microsaasadministrator.data.local.entity.WarehouseEntity

/**
 * Entidad Product - Representa un producto en una bodega.
 *
 * PRINCIPIO CLAVE (según documentación):
 * "El stock NO se guarda como campo"
 * "Se calcula a partir de los movimientos (entradas, salidas, ajustes)"
 *
 * Esto significa:
 * - Esta entidad define QUÉ es el producto
 * - Los movimientos (tabla movements) registran CAMBIOS
 * - El stock actual = SUM(movimientos.cantidad) para este producto
 *
 * Características:
 * - Cada producto pertenece a una bodega y empresa
 * - Tiene un stock mínimo para alertas
 * - Puede tener un proveedor asociado
 * - Los campos son inmutables (no cambian, se crea uno nuevo si cambia)
 *
 * @param id UUID único del producto
 * @param warehouseId FK a bodega
 * @param companyId FK a empresa
 * @param name Nombre del producto
 * @param unit Unidad de medida
 * @param minStock Stock mínimo para alertas
 * @param providerName Nombre del proveedor
 * @param notes Notas adicionales
 * @param isActive Si está activo
 * @param isSynced Flag de sincronización
 * @param createdAt Timestamp
 * @param updatedAt Timestamp
 */
@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = WarehouseEntity::class,
            parentColumns = ["id"],
            childColumns = ["warehouse_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CompanyEntity::class,
            parentColumns = ["id"],
            childColumns = ["company_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["warehouse_id"]),        // Buscar productos por bodega
        Index(value = ["company_id"]),          // Buscar productos por empresa
        Index(value = ["name"]),                // Buscar productos por nombre
        Index(value = ["warehouse_id", "name"]) // Nombre único por bodega
    ]
)
data class ProductEntity(
    @PrimaryKey
    val id: String = "",

    // FK: ID de la bodega donde está el producto
    @ColumnInfo(name = "warehouse_id")
    val warehouseId: String = "",

    // FK: ID de la empresa propietaria
    @ColumnInfo(name = "company_id")
    val companyId: String = "",

    // Nombre del producto (ej: "Harina de Trigo Premium")
    val name: String = "",

    // Unidad de medida (ej: "kg", "L", "Docena", "Bolsa")
    val unit: String = "",

    // Stock mínimo que dispara alerta (ej: 10)
    @ColumnInfo(name = "min_stock")
    val minStock: Int = 0,

    // Nombre del proveedor (opcional)
    @ColumnInfo(name = "provider_name")
    val providerName: String? = null,

    // Notas adicionales (ej: "Guardar en lugar frío")
    val notes: String? = null,

    // Si está activo (permite desactivar sin borrar histórico)
    val isActive: Boolean = true,

    // Flag de sincronización
    @ColumnInfo(name = "is_synced")
    val isSynced: Boolean = false,

    // Timestamp de creación
    val createdAt: Long = System.currentTimeMillis(),

    // Timestamp de última actualización (solo metadatos, no el stock)
    val updatedAt: Long = System.currentTimeMillis()
) {

    /**
     * Valida campos requeridos
     */
    fun isValid(): Boolean {
        return id.isNotBlank() &&
                warehouseId.isNotBlank() &&
                companyId.isNotBlank() &&
                name.isNotBlank() &&
                unit.isNotBlank() &&
                minStock >= 0
    }
}