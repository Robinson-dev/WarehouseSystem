# 📦 SISTEMA DE GESTIÓN DE BODEGA v1.0

## 🎯 DESCRIPCIÓN GENERAL

Sistema de gestión de inventarios para bodegas/almacenes que permite:

✅ **Gestión de Bodegas** - Crear, listar, ver detalles
✅ **Gestión de Productos** - Catálogo completo de productos
✅ **Control de Stock** - Entrada, salida, reserva, liberación
✅ **Auditoría Completa** - Registro de todos los movimientos
✅ **Alertas de Bajo Stock** - Identificación automática
✅ **Reportes Excel** - 4 hojas con análisis profesional
✅ **Navegación Multi-pantalla** - 6 pantallas interconectadas
✅ **Base de Datos Local** - Room Database con SQLite

---

## ARQUITECTURA

```
CLEAN ARCHITECTURE + MVVM

┌─────────────────────────────────┐
│   PRESENTATION LAYER (UI)       │
│   - Composables                 │
│   - ViewModels                  │
│   - Navigation                  │
└─────────────────────────────────┘
            ↕
┌─────────────────────────────────┐
│   DOMAIN LAYER (Lógica)         │
│   - Use Cases                   │
│   - Modelos                     │
└─────────────────────────────────┘
            ↕
┌─────────────────────────────────┐
│   DATA LAYER (Persistencia)     │
│   - Room Database               │
│   - DAOs                        │
│   - Entities                    │
└─────────────────────────────────┘
```

---

## ESTRUCTURA DE CARPETAS

```
WarehouseSystem/
│
├── app/
│   ├── src/main/
│   │   ├── AndroidManifest.xml
│   │   │
│   │   ├── java/com/warehouse/system/
│   │   │   │
│   │   │   ├── MainActivity.kt                [Punto entrada]
│   │   │   │
│   │   │   ├── data/                         [DATA LAYER]
│   │   │   │   ├── local/
│   │   │   │   │   ├── db/
│   │   │   │   │   │   ├── BodegaDatabase.kt
│   │   │   │   │   │   └── DatabaseModule.kt
│   │   │   │   │   ├── dao/
│   │   │   │   │   │   ├── ProductoDao.kt
│   │   │   │   │   │   ├── BodegaDao.kt
│   │   │   │   │   │   ├── ItemInventarioDao.kt
│   │   │   │   │   │   └── MovimientoDao.kt
│   │   │   │   │   └── entity/
│   │   │   │   │       ├── ProductoEntity.kt
│   │   │   │   │       ├── BodegaEntity.kt
│   │   │   │   │       ├── ItemInventarioEntity.kt
│   │   │   │   │       └── MovimientoEntity.kt
│   │   │   │   └── repository/
│   │   │   │       └── BodegaRepository.kt
│   │   │   │
│   │   │   ├── domain/                       [DOMAIN LAYER]
│   │   │   │   ├── model/
│   │   │   │   │   ├── Producto.kt
│   │   │   │   │   ├── Bodega.kt
│   │   │   │   │   ├── ItemInventario.kt
│   │   │   │   │   └── Movimiento.kt
│   │   │   │   │
│   │   │   │   └── usecase/
│   │   │   │       ├── bodega/
│   │   │   │       │   ├── CrearBodegaUseCase.kt
│   │   │   │       │   ├── ObtenerBodegasUseCase.kt
│   │   │   │       │   └── ObtenerBodegaPorIdUseCase.kt
│   │   │   │       ├── producto/
│   │   │   │       │   ├── AgregarProductoUseCase.kt
│   │   │   │       │   ├── ObtenerProductosUseCase.kt
│   │   │   │       │   └── BuscarProductoPorIdUseCase.kt
│   │   │   │       ├── inventario/
│   │   │   │       │   ├── ProcesarEntradaUseCase.kt
│   │   │   │       │   ├── ProcesarSalidaUseCase.kt
│   │   │   │       │   ├── ReservarStockUseCase.kt
│   │   │   │       │   ├── LiberarReservaUseCase.kt
│   │   │   │       │   ├── ObtenerInventarioBodegaUseCase.kt
│   │   │   │       │   └── ObtenerProductosBajoStockUseCase.kt
│   │   │   │       └── reporte/
│   │   │   │           ├── GenerarReporteExcelUseCase.kt
│   │   │   │           └── ObtenerHistorialMovimientosUseCase.kt
│   │   │   │
│   │   │   ├── presentation/                 [PRESENTATION LAYER]
│   │   │   │   ├── viewmodel/
│   │   │   │   │   ├── BodegaViewModel.kt
│   │   │   │   │   ├── ProductoViewModel.kt
│   │   │   │   │   ├── InventarioViewModel.kt
│   │   │   │   │   └── ReporteViewModel.kt
│   │   │   │   │
│   │   │   │   └── ui/
│   │   │   │       ├── screen/
│   │   │   │       │   ├── principal/
│   │   │   │       │   │   ├── PantallaPrincipal.kt
│   │   │   │       │   │   └── PantallaPrincipalComponents.kt
│   │   │   │       │   ├── bodega/
│   │   │   │       │   │   ├── PantallaCrearBodega.kt
│   │   │   │       │   │   ├── PantallaDetalleBodega.kt
│   │   │   │       │   │   └── BodegaScreenComponents.kt
│   │   │   │       │   ├── inventario/
│   │   │   │       │   │   ├── PantallaDetalleProducto.kt
│   │   │   │       │   │   └── InventarioComponents.kt
│   │   │   │       │   ├── reporte/
│   │   │   │       │   │   ├── PantallaReportes.kt
│   │   │   │       │   │   └── ReporteComponents.kt
│   │   │   │       │   └── configuracion/
│   │   │   │       │       └── PantallaConfiguracion.kt
│   │   │   │       │
│   │   │   │       ├── navigation/
│   │   │   │       │   ├── Pantalla.kt
│   │   │   │       │   └── AppNavigation.kt
│   │   │   │       │
│   │   │   │       ├── theme/
│   │   │   │       │   ├── Color.kt
│   │   │   │       │   ├── Typography.kt
│   │   │   │       │   └── Theme.kt
│   │   │   │       │
│   │   │   │       ├── components/
│   │   │   │       │   ├── AppBar.kt
│   │   │   │       │   ├── Cards.kt
│   │   │   │       │   ├── Dialogs.kt
│   │   │   │       │   └── CommonComponents.kt
│   │   │   │       │
│   │   │   │       └── App.kt
│   │   │   │
│   │   │   ├── util/                         [UTILITIES]
│   │   │   │   ├── excel/
│   │   │   │   │   ├── ExcelReportGenerator.kt
│   │   │   │   │   ├── ExcelStyles.kt
│   │   │   │   │   └── ExcelSheets.kt
│   │   │   │   ├── file/
│   │   │   │   │   ├── FileManager.kt
│   │   │   │   │   └── FileShareManager.kt
│   │   │   │   ├── constants/
│   │   │   │   │   ├── AppConstants.kt
│   │   │   │   │   ├── ErrorMessages.kt
│   │   │   │   │   └── SuccessMessages.kt
│   │   │   │   └── extension/
│   │   │   │       ├── DateExtension.kt
│   │   │   │       ├── NumberExtension.kt
│   │   │   │       └── StringExtension.kt
│   │   │   │
│   │   │   └── di/                           [INYECCIÓN DEPENDENCIAS]
│   │   │       ├── AppModule.kt
│   │   │       ├── DataModule.kt
│   │   │       ├── DomainModule.kt
│   │   │       └── PresentationModule.kt
│   │   │
│   │   └── res/
│   │       ├── values/
│   │       │   ├── strings.xml
│   │       │   ├── colors.xml
│   │       │   └── dimens.xml
│   │       ├── xml/
│   │       │   └── file_paths.xml
│   │       └── drawable/
│   │           └── ic_launcher.xml
│   │
│   └── build.gradle.kts
│
├── docs/                                    [DOCUMENTACIÓN]
│   ├── ARQUITECTURA.md
│   ├── GUIA_INSTALACION.md
│   ├── GUIA_USO.md
│   ├── API_REFERENCE.md
│   └── TROUBLESHOOTING.md
│
└── README.md
```

---

## 🚀 INSTALACIÓN PASO A PASO

### Requisitos Previos
- Android Studio Flamingo o superior
- Android SDK 24+ (Android 7.0+)
- Java 11 o superior
- Gradle 8.0+

### Pasos de Instalación

#### 1️⃣ Crear Proyecto Base
```bash
# En Android Studio
File → New → New Android Project
- Name: WarehouseSystem
- Package: com.warehouse.system
- Min API: 24 (Android 7.0)
- Language: Kotlin
```


## 📱 USO DE LA APLICACIÓN

### Flujo Principal de Usuario

```
1. PANTALLA PRINCIPAL
   ├─ Listar todas las bodegas
   ├─ Botón: Crear Nueva Bodega
   └─ Click en bodega → Detalle

2. CREAR BODEGA
   ├─ Ingresar nombre
   ├─ Ingresar ubicación
   ├─ Ingresar capacidad máxima
   └─ Guardar → BD

3. DETALLE BODEGA
   ├─ Ver información de bodega
   ├─ Listar productos
   ├─ Botón: Agregar Producto
   ├─ Botón: Registrar Movimiento
   ├─ Ver productos con bajo stock
   └─ Click en producto → Detalles

4. AGREGAR PRODUCTO
   ├─ Ingr esar datos:
   │  ├─ Nombre
   │  ├─ SKU
   │  ├─ Precio
   │  ├─ Categoría
   │  ├─ Proveedor
   │  ├─ Cantidad Inicial
   │  └─ Stock Mínimo
   └─ Guardar → BD

5. DETALLE PRODUCTO
   ├─ Ver información completa
   ├─ Ver estado del stock
   ├─ Entrada de stock
   ├─ Salida de stock
   ├─ Reservar stock
   └─ Liberar reserva

6. REPORTES
   ├─ Botón: Generar Reporte Excel
   ├─ Seleccionar opciones
   ├─ Descargar archivo
   ├─ Abrir con Excel
   ├─ Compartir por email
   └─ Ver reportes anteriores
```

### Operaciones de Stock

**Entrada:**
```
Compra a proveedor → Click "Entrada" 
→ Ingresar cantidad → Confirmar 
→ Stock aumenta
```

**Salida:**
```
Venta a cliente → Click "Salida"
→ Ingresar cantidad → Confirmar
→ Stock disminuye (con validación)
```

**Reserva:**
```
Pedido pendiente → Click "Reservar"
→ Ingresar cantidad → Confirmar
→ Stock se marca como no disponible
```

**Liberación:**
```
Cliente cancela → Click "Liberar"
→ Ingresar cantidad → Confirmar
→ Stock vuelve a estar disponible
```

---

## EXPORTACIÓN A EXCEL

### Contenido del Reporte

**Hoja 1: Resumen**
- Información de bodega
- KPIs principales (totales, valores, alertas)
- Estadísticas generales

**Hoja 2: Inventario Detallado**
- Tabla completa de productos
- Todas las columnas de stock
- Fórmulas de totales

**Hoja 3: ⚠️ Bajo Stock**
- Solo productos con alerta
- Recomendaciones de reorden
- Formato destacado en rojo

**Hoja 4: Análisis por Categoría**
- Agrupación por categoría
- Totales por categoría
- Análisis comparativo

### Descargar Reporte
1. En pantalla de Reportes
2. Click "Descargar Reporte Excel"
3. Esperar generación (progreso)
4. Opciones: Abrir o Compartir
5. Archivo guardado en `/Documentos/BodegaReports/`

---

## 🔐 CARACTERÍSTICAS DE SEGURIDAD

✅ Base de datos local encriptada
✅ Validación de datos en entrada
✅ Control de permisos de archivos
✅ Registro de auditoría completo
✅ Foreign keys en BD para integridad

---

## 📈 ESCALABILIDAD FUTURA

El sistema está diseñado para agregar:

1. **API REST** - Sincronizar con servidor
2. **Autenticación** - Login de usuarios
3. **Múltiples Usuarios** - Control de permisos
4. **Reportes PDF** - Alternativa a Excel
5. **Gráficos** - Visualización de datos
6. **Predicción de Demanda** - ML/Analytics
7. **Mobile App iOS** - Multiplataforma
8. **Sincronización en Nube** - Cloud backup

---

##  SOLUCIÓN DE PROBLEMAS

| Problema | Solución |
|----------|----------|
| BD no se crea | Verificar permisos en AndroidManifest |
| Excel no se descarga | Verificar permisos de almacenamiento |
| App se cierra | Ver Logcat para excepciones |
| No se ve inventario | Agregar productos primero |
| Bajo rendimiento | Limpiar datos antiguos |


## 🎓 TECNOLOGÍAS UTILIZADAS

- **Kotlin** - Lenguaje principal
- **Jetpack Compose** - UI declarativa
- **Room Database** - Persistencia local
- **Navigation Compose** - Navegación
- **Coroutines** - Asincronía
- **Apache POI** - Exportación Excel
- **Material Design 3** - Diseño UI

