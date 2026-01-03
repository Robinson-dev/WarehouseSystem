package cl.arriagada.microsaasadministrator.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import cl.arriagada.microsaasadministrator.data.local.dao.ProductEntity

/**
 * Entidad Movement - Registro INMUTABLE de un movimiento de inventario.
 *
 * PRINCIPIOS CLAVE (según documentación):
 *
 * 1. "Los movimientos NUNCA se editan"
 *    - Esto garantiza trazabilidad completa y auditoría perfecta
 *    - Si hay error, se registra un movimiento de ajuste
 *    - NO se modifican movimientos existentes
 *
 * 2. "El stock se calcula a partir de movimientos"
 *    - Stock actual = SUM(movements.quantity) para un producto en una bodega
 *    - ENTRADA (+): Compra, recepción
 *    - SALIDA (-): Consumo, uso, venta
 *    - AJUSTE (±): Correcciones, merma, inventario
 *
 * Características:
 * - Cada movimiento es un evento único e irrepetible
 * - Contiene referencia a producto, bodega, empresa
 * - Registra el cambio (cantidad) no el estado (stock)
 * - Incluye metadata: fecha, hora, observaciones
 * - Flag de sincronización para offline-first
 *
 * @param id UUID único del movimiento
 * @param productId FK a producto
 * @param warehouseId FK a bodega
 * @param companyId FK a empresa
 * @param type ENTRADA, SALIDA, o AJUSTE
 * @param quantity Cantidad (siempre positiva, el tipo indica dirección)
 * @param unit Unidad de medida (heredada del producto)
 * @param date Fecha del movimiento (ISO format: YYYY-MM-DD)
 * @param time Hora del movimiento (HH:mm)
 * @param notes Observaciones opcionales
 * @param isSynced Flag de sincronización
 * @param createdAt Timestamp (cuando se creó el registro en la BD local)
 */
@Entity(
    tableName = "movements",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["product_id"],
            onDelete = ForeignKey.CASCADE
        ),
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
        Index(value = ["product_id"]),          // Buscar movimientos por producto
        Index(value = ["warehouse_id"]),        // Buscar movimientos por bodega
        Index(value = ["company_id"]),          // Buscar movimientos por empresa
        Index(value = ["createdAt"]),           // Buscar movimientos recientes
        Index(value = ["product_id", "warehouse_id"]) // Stock actual
    ]
)
data class MovementEntity(
    @PrimaryKey
    val id: String = "",

    // FK: ID del producto afectado
    @ColumnInfo(name = "product_id")
    val productId: String = "",

    // FK: ID de la bodega donde ocurre el movimiento
    @ColumnInfo(name = "warehouse_id")
    val warehouseId: String = "",

    // FK: ID de la empresa
    @ColumnInfo(name = "company_id")
    val companyId: String = "",

    // Tipo de movimiento: "ENTRADA", "SALIDA", "AJUSTE"
    // ENTRADA: Compra, recepción (stock sube)
    // SALIDA: Consumo, uso, venta (stock baja)
    // AJUSTE: Corrección de inventario, merma detectada
    val type: String = "",

    // Cantidad del movimiento (SIEMPRE POSITIVA)
    // El cálculo de stock se hace con: SUM(quantity) respetando el type
    val quantity: Double = 0.0,

    // Unidad de medida (ej: "kg", "L", heredada del producto)
    val unit: String = "",

    // Fecha del movimiento (ISO format: YYYY-MM-DD)
    // Ejemplo: "2026-02-01"
    val date: String = "",

    // Hora del movimiento (formato HH:mm)
    // Ejemplo: "14:30"
    val time: String = "",

    // Observaciones o notas sobre el movimiento (opcional)
    // Ejemplos:
    // - "Entrega Molina"
    // - "Preparación del día"
    // - "Merma por vencimiento"
    val notes: String? = null,

    // Flag de sincronización con Supabase
    // false = creado localmente, pendiente de enviar
    // true = ya sincronizado con servidor
    @ColumnInfo(name = "is_synced")
    val isSynced: Boolean = false,

    // Timestamp de creación (cuándo se registró en BD local)
    // NO tiene updatedAt porque es INMUTABLE
    val createdAt: Long = System.currentTimeMillis()
) {

    /**
     * Valida que los campos requeridos sean válidos
     */
    fun isValid(): Boolean {
        return id.isNotBlank() &&
                productId.isNotBlank() &&
                warehouseId.isNotBlank() &&
                companyId.isNotBlank() &&
                type in listOf("ENTRADA", "SALIDA", "AJUSTE") &&
                quantity > 0.0 &&
                unit.isNotBlank() &&
                date.isNotBlank() &&
                time.isNotBlank()
    }

    /**
     * Retorna el signo del movimiento para cálculos
     * Positivo para ENTRADA y AJUSTE, negativo para SALIDA
     */
    fun getSignedQuantity(): Double {
        return when (type) {
            "ENTRADA" -> quantity
            "SALIDA" -> -quantity
            "AJUSTE" -> quantity // El signo está en la cantidad
            else -> 0.0
        }
    }
}

/**
 * Enum para tipos de movimiento (más type-safe que strings)
 */
enum class MovementType(val value: String) {
    ENTRADA("ENTRADA"),
    SALIDA("SALIDA"),
    AJUSTE("AJUSTE");

    companion object {
        fun fromString(value: String): MovementType? {
            return values().find { it.value == value }
        }
    }
}