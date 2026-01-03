plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "cl.arriagada.multiplescronos"
    compileSdk = 36

    defaultConfig {
        applicationId = "cl.arriagada.multiplescronos"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        // Habilita el soporte para Compose
        compose = true
    }

    composeOptions {
        // Define la versión del compilador de Kotlin para Compose
        kotlinCompilerExtensionVersion = "1.5.1" // (O la versión actual compatible)
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // Dependencias de Compose
    implementation("androidx.core:core-ktx:1.9.0") // KTX para Kotlin
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2") // Soporte de Coroutines

    // Compose - UI (Elementos visuales)
    implementation("androidx.compose.ui:ui:1.5.4")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.4")

    // Compose - Material Design (Botones, AppBar, etc.)
    implementation("androidx.compose.material3:material3:1.1.2")

    // Dependencia necesaria para las coroutines de Kotlin Flow (usadas en Cronometro.kt)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Si estás usando StateFlow con Compose
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    implementation("androidx.compose.runtime:runtime-livedata:1.5.4")

    // ... otras dependencias
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.espresso.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}