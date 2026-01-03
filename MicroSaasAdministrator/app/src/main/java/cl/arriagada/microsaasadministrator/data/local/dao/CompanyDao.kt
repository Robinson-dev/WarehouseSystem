package cl.arriagada.microsaasadministrator.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cl.arriagada.microsaasadministrator.data.local.entity.CompanyEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) para operaciones con CompanyEntity.
 *
 * Room convierte estos métodos en consultas SQL automáticamente.
 * Todos los métodos que retornan datos usan Flow para reactividad.
 */
@Dao
interface CompanyDao {

    /**
     * Inserta una empresa nueva
     *
     * @param company Entidad de empresa a insertar
     * @return ID de la fila insertada
     */
    @Insert
    suspend fun insertCompany(company: CompanyEntity): Long

    /**
     * Actualiza una empresa existente
     *
     * @param company Entidad con datos actualizados
     */
    @Update
    suspend fun updateCompany(company: CompanyEntity)

    /**
     * Obtiene una empresa por su ID
     *
     * @param companyId ID único de la empresa
     * @return Flow que emite la empresa (o null si no existe)
     */
    @Query("SELECT * FROM companies WHERE id = :companyId")
    fun getCompanyById(companyId: String): Flow<CompanyEntity?>

    /**
     * Obtiene una empresa por el email del propietario
     *
     * @param email Email del propietario
     * @return Flow que emite la empresa (email es único)
     */
    @Query("SELECT * FROM companies WHERE ownerEmail = :email")
    fun getCompanyByEmail(email: String): Flow<CompanyEntity?>

    /**
     * Obtiene todas las empresas (útil para multi-account)
     * Ordenadas por última actualización (más reciente primero)
     *
     * @return Flow que emite lista de empresas
     */
    @Query("SELECT * FROM companies ORDER BY updatedAt DESC")
    fun getAllCompanies(): Flow<List<CompanyEntity>>

    /**
     * Obtiene todas las empresas activas
     *
     * @return Flow que emite lista de empresas activas
     */
    @Query("SELECT * FROM companies WHERE isActive = 1 ORDER BY updatedAt DESC")
    fun getActiveCompanies(): Flow<List<CompanyEntity>>

    /**
     * Obtiene empresas no sincronizadas (para enviar a Supabase)
     *
     * @return Flow que emite empresas pendientes de sincronización
     */
    @Query("SELECT * FROM companies WHERE is_synced = 0 ORDER BY createdAt ASC")
    fun getUnsyncedCompanies(): Flow<List<CompanyEntity>>

    /**
     * Marca una empresa como sincronizada
     *
     * @param companyId ID de la empresa
     */
    @Query("UPDATE companies SET is_synced = 1, updatedAt = :timestamp WHERE id = :companyId")
    suspend fun markAsSynced(companyId: String, timestamp: Long = System.currentTimeMillis())

    /**
     * Verifica si existe una empresa con cierto email
     *
     * @param email Email a buscar
     * @return true si existe, false en caso contrario
     */
    @Query("SELECT EXISTS(SELECT 1 FROM companies WHERE ownerEmail = :email)")
    suspend fun emailExists(email: String): Boolean

    /**
     * Obtiene el count de empresas activas
     *
     * @return Flow que emite el número de empresas activas
     */
    @Query("SELECT COUNT(*) FROM companies WHERE isActive = 1")
    fun getActiveCompanyCount(): Flow<Int>

    /**
     * Elimina una empresa (borra físicamente)
     * CUIDADO: Esto cascada a todas las bodegas, productos y movimientos
     *
     * @param company Entidad a eliminar
     */
    @Delete
    suspend fun deleteCompany(company: CompanyEntity)

    /**
     * Limpia todos los datos de empresas
     * PELIGROSO: Usar solo en testing o reset
     */
    @Query("DELETE FROM companies")
    suspend fun deleteAllCompanies()
}