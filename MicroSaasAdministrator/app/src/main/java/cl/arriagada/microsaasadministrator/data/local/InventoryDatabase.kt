package cl.arriagada.microsaasadministrator.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import cl.arriagada.microsaasadministrator.data.local.dao.ProductEntity
import cl.arriagada.microsaasadministrator.data.local.entity.CompanyEntity
import cl.arriagada.microsaasadministrator.data.local.entity.MovementEntity
import cl.arriagada.microsaasadministrator.data.local.entity.WarehouseEntity

import timber.log.Timber

/**
 * Clase abstracta que representa la base de datos Room principal.
 *
 * @Database anotación define:
 * - version: 1 - Primera versión del esquema (incrementar si cambia)
 * - entities: Las 4 entidades principales del sistema
 * - exportSchema: false - No exportar esquema JSON en esta fase
 *
 * ESTRUCTURA DE DATOS:
 * Company (empresa)
 *   ├── Warehouse (bodega) - FK a Company
 *   │   └── Product (producto) - FK a Warehouse
 *   │       └── Movement (movimiento) - FK a Product
 *   └── User (usuarios de la empresa)
 *
 * CARACTERÍSTICAS:
 * - Multi-tenant: datos aislados por empresa
 * - Offline-first: Room es la fuente de verdad
 * - Inmutable: Movimientos nunca se editan
 * - Stock calculado: No se guarda, se calcula desde movimientos
 */
@Database(
    entities = [
        CompanyEntity::class,       // Empresas
        WarehouseEntity::class,     // Bodegas
        ProductEntity::class,       // Productos
        MovementEntity::class       // Movimientos de inventario
    ],
    version = 1,
    exportSchema = false
)
abstract class InventoryDatabase : RoomDatabase() {

    // DAOs - Cada uno maneja una entidad

    /**
     * DAO para operaciones con Company
     */
    abstract fun companyDao(): CompanyDao

    /**
     * DAO para operaciones con Warehouse
     */
    abstract fun warehouseDao(): WarehouseDao

    /**
     * DAO para operaciones con Product
     */
    abstract fun productDao(): ProductDao

    /**
     * DAO para operaciones con Movement
     */
    abstract fun movementDao(): MovementDao

    companion object {
        // Instancia singleton de la base de datos
        @Volatile
        private var INSTANCE: InventoryDatabase? = null

        // Nombre del archivo de base de datos
        private const val DATABASE_NAME = "inventory_database"

        /**
         * Obtiene o crea una instancia de la base de datos.
         * Utiliza Double-Checked Locking para thread-safety.
         *
         * @param context Contexto de la aplicación
         * @return Instancia de InventoryDatabase (singleton)
         */
        fun getDatabase(context: Context): InventoryDatabase {
            // Si ya existe instancia, la retorna directamente (fast path)
            return INSTANCE ?: synchronized(this) {
                // Double-check porque el INSTANCE podría haber sido creado en otro thread
                val existing = INSTANCE
                if (existing != null) {
                    return@synchronized existing
                }

                // Crea la base de datos
                Timber.d("Creando instancia de InventoryDatabase...")
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    InventoryDatabase::class.java,
                    DATABASE_NAME
                )
                    // Configuración de callbacks y migraciones
                    .addCallback(RoomCallback())
                    // Limpia la BD si cambia la versión (para fase 0)
                    // IMPORTANTE: En producción, implementar migrations reales
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                Timber.d("InventoryDatabase creada exitosamente")
                instance
            }
        }

        /**
         * Limpia la instancia (para testing)
         */
        fun clearInstance() {
            INSTANCE = null
        }
    }

    /**
     * Callbacks para eventos del ciclo de vida de la BD
     */
    class RoomCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Timber.d("Tabla de Room creada")
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            Timber.d("Base de datos abierta")
        }
    }
}