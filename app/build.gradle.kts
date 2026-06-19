import java.util.Properties
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}


plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.googleServices)
}

android {
    namespace = "com.example.nexum_trabajador"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.nexum_trabajador"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


        resValue("string", "maps_api_key", localProperties.getProperty("MAPS_API_KEY") ?: "")

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
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // Base
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.common.android)
    implementation(libs.androidx.compose.foundation)
    // Fonts
    implementation(libs.androidx.ui.text.google.fonts)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // Hilt Nav
    implementation(libs.androidx.hilt.navigation.compose)

    // Navigation
    implementation(libs.androidx.navigation)
    implementation(libs.kotlinx.serialization.json)

    // Icons
    implementation(libs.androidx.material.icons.extended)

    // OKhttp
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.logging)
    implementation(libs.sandwich.ktor)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.slf4j.simple)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Datastore
    implementation(libs.androidx.datastore.preferences)

    // Splash
    implementation(libs.androidx.core.splashscreen)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.storage)
    implementation("com.google.firebase:firebase-appcheck-debug")
    implementation("com.google.firebase:firebase-appcheck-playintegrity")


    //Room
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)

    //Glide
    implementation(libs.glide.compose)

    // Swipe
    implementation(libs.accompanist.swiperefresh)


    // Google Maps SDK for Android
    implementation(libs.places)
    implementation(libs.play.services.maps)

    // Google maps Compose
    implementation(libs.maps.compose)

    // Timber
    implementation(libs.timber)

    // Constraint layout
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.constraintlayout.compose)
}
