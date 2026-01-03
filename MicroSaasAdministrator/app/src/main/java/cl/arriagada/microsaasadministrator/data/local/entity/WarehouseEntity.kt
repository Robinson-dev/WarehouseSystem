package cl.arriagada.microsaasadministrator.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entidad Warehouse - Representa una bodega/almacén de la empresa.
 *
 * Características:
 * - Una empresa puede tener múltiples bodegas
 * - Cada bodega contiene productos
 * - Al crear empresa, se crea automáticamente una "Bodega Principal"
 * - Los movimientos se registran por bodega
 *
 * @param id UUID único de la bodega
 * @param companyId FK a la empresa propietaria
 * @param name Nombre único de la bodega dentro de la empresa
 * @param location Ubicación física (opcional)
 * @param responsible Persona responsable de la bodega (opcional)
 * @param isActive Si está activa
 * @param isSynced Flag de sincronización
 * @param createdAt Timestamp
 * @param updatedAt Timestamp
 */
@Entity(
    tableName = "warehouses",
    foreignKeys = [
        // Relación con Company: si se elimina la empresa, se eliminan sus bodegas
        ForeignKey(
            entity = CompanyEntity::class,
            parentColumns = ["id"],
            childColumns = ["company_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    // Índices para optimizar búsquedas
    indices = [
        Index(value = ["company_id"]),          // Buscar bodegas por empresa
        Index(value = ["company_id", "name"])   // Buscar bodega por nombre en empresa
    ]
)
data class WarehouseEntity(
    @PrimaryKey
    val id: String = "",

    // FK: ID de la empresa propietaria
    @ColumnInfo(name = "company_id")
    val companyId: String = "",

    // Nombre único de la bodega (ej: "Bodega Principal", "Bodega Secundaria")
    val name: String = "",

    // Ubicación física (ej: "Piso 2 - Esquina derecha")
    val location: String? = null,

    // Persona responsable (ej: "Juan Pérez")
    val responsible: String? = null,

    // Si está activa (permite desactivar sin borrar)
    val isActive: Boolean = true,

    // Flag de sincronización con Supabase
    @ColumnInfo(name = "is_synced")
    val isSynced: Boolean = false,

    // Timestamp de creación
    val createdAt: Long = System.currentTimeMillis(),

    // Timestamp de última actualización
    val updatedAt: Long = System.currentTimeMillis()
) {

    /**
     * Valida que los campos requeridos estén presentes
     */
    fun isValid(): Boolean {
        return id.isNotBlank() &&
                companyId.isNotBlank() &&
                name.isNotBlank()
    }
}