package cl.arriagada.microsaasadministrator.di

import android.content.Context
import cl.arriagada.microsaasadministrator.dao.CompanyDao
import cl.arriagada.microsaasadministrator.dao.InventoryDatabase
import cl.arriagada.microsaasadministrator.dao.MovementDao
import cl.arriagada.microsaasadministrator.dao.ProductDao
import cl.arriagada.microsaasadministrator.dao.WarehouseDao

import cl.arriagada.microsaasadministrator.data.remote.supabase.SupabaseConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideInventoryDatabase(
        @ApplicationContext context: Context
    ): InventoryDatabase {
        Timber.d("Inicializando InventoryDatabase...")
        return InventoryDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideCompanyDao(database: InventoryDatabase): CompanyDao {
        Timber.d("Inyectando CompanyDao")
        return database.companyDao()
    }

    @Provides
    @Singleton
    fun provideWarehouseDao(database: InventoryDatabase): WarehouseDao {
        Timber.d("Inyectando WarehouseDao")
        return database.warehouseDao()
    }

    @Provides
    @Singleton
    fun provideProductDao(database: InventoryDatabase): ProductDao {
        Timber.d("Inyectando ProductDao")
        return database.productDao()
    }

    @Provides
    @Singleton
    fun provideMovementDao(database: InventoryDatabase): MovementDao {
        Timber.d("Inyectando MovementDao")
        return database.movementDao()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor { message ->
            Timber.tag("Supabase-HTTP").d(message)
        }.apply {
            level = if (SupabaseConfig.ENABLE_SUPABASE_LOGS) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

    @Provides
    @Singleton
    fun provideSupabaseInterceptor(): Interceptor = Interceptor { chain ->
        val originalRequest = chain.request()

        if (!SupabaseConfig.isConfigured()) {
            Timber.w("⚠️ Supabase no está configurada correctamente")
            Timber.w(SupabaseConfig.getConfigurationError())
        }

        val requestBuilder = originalRequest.newBuilder()
            .header("apikey", SupabaseConfig.SUPABASE_ANON_KEY)
            .header("Content-Type", "application/json")

        Timber.d("Request: ${originalRequest.method} ${originalRequest.url}")

        val response = chain.proceed(requestBuilder.build())

        Timber.d("Response: ${response.code} ${response.message}")

        response
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        supabaseInterceptor: Interceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(supabaseInterceptor)
        .addNetworkInterceptor(loggingInterceptor)
        .connectTimeout(SupabaseConfig.NETWORK_TIMEOUT_MS, java.util.concurrent.TimeUnit.MILLISECONDS)
        .readTimeout(SupabaseConfig.NETWORK_TIMEOUT_MS, java.util.concurrent.TimeUnit.MILLISECONDS)
        .writeTimeout(SupabaseConfig.NETWORK_TIMEOUT_MS, java.util.concurrent.TimeUnit.MILLISECONDS)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        Timber.d("Inicializando Retrofit con URL: ${SupabaseConfig.SUPABASE_URL}")

        return Retrofit.Builder()
            .baseUrl(SupabaseConfig.SUPABASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

}