package cl.arriagada.microsaasadministrator.data.remote.supabase.dto

import cl.arriagada.microsaasadministrator.data.local.dao.ProductEntity
import cl.arriagada.microsaasadministrator.data.local.entity.CompanyEntity
import cl.arriagada.microsaasadministrator.data.local.entity.MovementEntity
import cl.arriagada.microsaasadministrator.data.local.entity.WarehouseEntity
import com.google.gson.annotations.SerializedName


/**
 * DTOs (Data Transfer Objects) para sincronización con Supabase
 *
 * Estos modelos representan cómo se transmiten los datos a/desde el servidor.
 * Son diferentes de las Entities de Room porque:
 * - Pueden tener nombres de campos diferentes (con @SerializedName)
 * - Pueden incluir/excluir campos específicos
 * - Facilitan la versioning de la API
 */

// ============== COMPANY DTO ==============

/**
 * DTO para sincronizar Company con Supabase
 */
data class CompanyDto(
    val id: String = "",
    val name: String = "",

    @SerializedName("business_type")
    val businessType: String = "",

    @SerializedName("owner_name")
    val ownerName: String = "",

    @SerializedName("owner_email")
    val ownerEmail: String = "",

    @SerializedName("is_active")
    val isActive: Boolean = true,

    @SerializedName("created_at")
    val createdAt: String = "",

    @SerializedName("updated_at")
    val updatedAt: String = ""
) {
    /**
     * Convierte DTO a Entity de Room
     */
    fun toEntity(): CompanyEntity {
        return CompanyEntity(
            id = id,
            name = name,
            businessType = businessType,
            ownerName = ownerName,
            ownerEmail = ownerEmail,
            isActive = isActive,
            isSynced = true,
            createdAt = createdAt.toLongOrNull() ?: System.currentTimeMillis(),
            updatedAt = updatedAt.toLongOrNull() ?: System.currentTimeMillis()
        )
    }
}

/**
 * Extensión para convertir Entity a DTO
 */
fun CompanyEntity.toDto(): CompanyDto {
    return CompanyDto(
        id = id,
        name = name,
        businessType = businessType,
        ownerName = ownerName,
        ownerEmail = ownerEmail,
        isActive = isActive,
        createdAt = createdAt.toString(),
        updatedAt = updatedAt.toString()
    )
}

// ============== WAREHOUSE DTO ==============

data class WarehouseDto(
    val id: String = "",

    @SerializedName("company_id")
    val companyId: String = "",

    val name: String = "",
    val location: String? = null,
    val responsible: String? = null,

    @SerializedName("is_active")
    val isActive: Boolean = true,

    @SerializedName("created_at")
    val createdAt: String = "",

    @SerializedName("updated_at")
    val updatedAt: String = ""
) {
    fun toEntity(): WarehouseEntity {
        return WarehouseEntity(
            id = id,
            companyId = companyId,
            name = name,
            location = location,
            responsible = responsible,
            isActive = isActive,
            isSynced = true,
            createdAt = createdAt.toLongOrNull() ?: System.currentTimeMillis(),
            updatedAt = updatedAt.toLongOrNull() ?: System.currentTimeMillis()
        )
    }
}

fun WarehouseEntity.toDto(): WarehouseDto {
    return WarehouseDto(
        id = id,
        companyId = companyId,
        name = name,
        location = location,
        responsible = responsible,
        isActive = isActive,
        createdAt = createdAt.toString(),
        updatedAt = updatedAt.toString()
    )
}

// ============== PRODUCT DTO ==============

data class ProductDto(
    val id: String = "",

    @SerializedName("warehouse_id")
    val warehouseId: String = "",

    @SerializedName("company_id")
    val companyId: String = "",

    val name: String = "",
    val unit: String = "",

    @SerializedName("min_stock")
    val minStock: Int = 0,

    @SerializedName("provider_name")
    val providerName: String? = null,

    val notes: String? = null,

    @SerializedName("is_active")
    val isActive: Boolean = true,

    @SerializedName("created_at")
    val createdAt: String = "",

    @SerializedName("updated_at")
    val updatedAt: String = ""
) {
    fun toEntity(): ProductEntity {
        return ProductEntity(
            id = id,
            warehouseId = warehouseId,
            companyId = companyId,
            name = name,
            unit = unit,
            minStock = minStock,
            providerName = providerName,
            notes = notes,
            isActive = isActive,
            isSynced = true,
            createdAt = createdAt.toLongOrNull() ?: System.currentTimeMillis(),
            updatedAt = updatedAt.toLongOrNull() ?: System.currentTimeMillis()
        )
    }
}

fun ProductEntity.toDto(): ProductDto {
    return ProductDto(
        id = id,
        warehouseId = warehouseId,
        companyId = companyId,
        name = name,
        unit = unit,
        minStock = minStock,
        providerName = providerName,
        notes = notes,
        isActive = isActive,
        createdAt = createdAt.toString(),
        updatedAt = updatedAt.toString()
    )
}

// ============== MOVEMENT DTO ==============

data class MovementDto(
    val id: String = "",

    @SerializedName("product_id")
    val productId: String = "",

    @SerializedName("warehouse_id")
    val warehouseId: String = "",

    @SerializedName("company_id")
    val companyId: String = "",

    val type: String = "",  // ENTRADA, SALIDA, AJUSTE
    val quantity: Double = 0.0,
    val unit: String = "",
    val date: String = "",
    val time: String = "",
    val notes: String? = null,

    @SerializedName("created_at")
    val createdAt: String = ""
) {
    fun toEntity(): MovementEntity {
        return MovementEntity(
            id = id,
            productId = productId,
            warehouseId = warehouseId,
            companyId = companyId,
            type = type,
            quantity = quantity,
            unit = unit,
            date = date,
            time = time,
            notes = notes,
            isSynced = true,
            createdAt = createdAt.toLongOrNull() ?: System.currentTimeMillis()
        )
    }
}

fun MovementEntity.toDto(): MovementDto {
    return MovementDto(
        id = id,
        productId = productId,
        warehouseId = warehouseId,
        companyId = companyId,
        type = type,
        quantity = quantity,
        unit = unit,
        date = date,
        time = time,
        notes = notes,
        createdAt = createdAt.toString()
    )
}

// ============== SYNC RESPONSE ==============

/**
 * Respuesta genérica de Supabase
 */
data class SupabaseResponse<T>(
    val data: T? = null,
    val error: SupabaseError? = null,

    @SerializedName("error_description")
    val errorDescription: String? = null
) {
    fun isSuccess(): Boolean = error == null && errorDescription == null
}

/**
 * Información de error de Supabase
 */
data class SupabaseError(
    val code: String = "",
    val message: String = "",
    val details: String? = null
)

/**
 * Respuesta de sincronización
 */
data class SyncResponse(
    @SerializedName("success_count")
    val successCount: Int = 0,

    @SerializedName("error_count")
    val errorCount: Int = 0,

    val timestamp: String = "",
    val errors: List<String> = emptyList()
)