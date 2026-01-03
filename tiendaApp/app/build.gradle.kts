plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp") version "2.0.21-1.0.25"
    kotlin("plugin.serialization")
}

android {
    namespace = "duoc.desarrollomobile.tiendapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "duoc.desarrollomobile.tiendapp"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    // --- CORRECCIÓN 1: AÑADIR composeOptions ---
    composeOptions {
        kotlinCompilerExtensionVersion = "2.0.0" // Reemplaza con la versión compatible de tu BOM
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // --- CORRECCIÓN 2: AÑADIR DEPENDENCIA DE VIEWMODEL ---
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.2") // Puedes usar una versión más reciente si la hay

// ViewModel para Compose
    implementation(libs.lifecycle.viewmodel.compose)
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.9.4")

    // Material Icons Extended (para iconos)
    implementation("androidx.compose.material:material-icons-extended:1.7.7")

    // Room Database
    implementation("androidx.room:room-runtime:2.7.0-alpha12")
    implementation("androidx.room:room-ktx:2.7.0-alpha12")
    ksp("androidx.room:room-compiler:2.7.0-alpha12")

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.9.0-alpha03")

    // Coil para cargar imágenes
    implementation("io.coil-kt:coil-compose:2.5.0")

    // Kotlinx Serialization para JSON
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
}


