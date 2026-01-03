package cl.arriagada.microsaasadministrator.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Company - Representa una empresa en el sistema.
 *
 * Características:
 * - Cada usuario pertenece a una empresa
 * - Multi-tenant: los datos están completamente aislados por empresa
 * - Sincronizados con Supabase
 *
 * @param id UUID único (mismo que en Supabase)
 * @param name Nombre de la empresa (ej: "Mi Cocina SRL")
 * @param businessType Tipo de negocio (ej: "Restaurante", "Pastelería")
 * @param ownerName Nombre del propietario
 * @param ownerEmail Email único del propietario
 * @param isActive Si la empresa está activa
 * @param synced Flag indicador si está sincronizado con Supabase
 * @param createdAt Timestamp de creación
 * @param updatedAt Timestamp de última actualización
 */
@Entity(tableName = "companies")
data class CompanyEntity(
    @PrimaryKey
    val id: String = "",

    // Nombre de la empresa
    val name: String = "",

    // Tipo de negocio (Restaurante, Pastelería, Supermercado, etc)
    val businessType: String = "",

    // Nombre del propietario/admin
    val ownerName: String = "",

    // Email del propietario (único en el sistema)
    val ownerEmail: String = "",

    // Indica si la empresa está activa
    val isActive: Boolean = true,

    // Flag de sincronización: true = sincronizado con Supabase, false = pendiente
    @ColumnInfo(name = "is_synced")
    val isSynced: Boolean = false,

    // Timestamp de creación (milliseconds desde epoch)
    val createdAt: Long = System.currentTimeMillis(),

    // Timestamp de última actualización (milliseconds desde epoch)
    val updatedAt: Long = System.currentTimeMillis()
) {

    /**
     * Verifica si los datos básicos son válidos
     */
    fun isValid(): Boolean {
        return id.isNotBlank() &&
                name.isNotBlank() &&
                ownerEmail.isNotBlank() &&
                businessType.isNotBlank()
    }
}