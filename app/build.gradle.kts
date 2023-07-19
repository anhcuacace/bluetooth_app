@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
//    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}

android {
    namespace = "tunanh.test_app"
    compileSdk = 33

    defaultConfig {
        applicationId = "tunanh.test_app"
        minSdk = 28
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

//    implementation(libs.kotlin.stdlib)
//    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.app.compat)
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.material)
    implementation(libs.material.icon)
    implementation(libs.datastore.preferences)
//    implementation(files("/libs/boltsdk-release-3.0.86.aar"))
    implementation(files("libs/database-connection-1.00.jar"))
    implementation(files("libs/Universal_SDK_1.00.164_os.jar"))
    implementation("com.google.accompanist:accompanist-permissions:0.30.1")
    implementation("androidx.navigation:navigation-common-ktx:2.6.0")
    implementation("androidx.navigation:navigation-runtime-ktx:2.6.0")
    implementation("androidx.navigation:navigation-compose:2.6.0")
    implementation(libs.coroutines.core)
//    implementation(libs.coroutines.flow)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp3)
    implementation(libs.logging.interceptor)
    implementation(libs.gson)
    implementation(libs.timber)
    implementation(platform("androidx.compose:compose-bom:2022.10.00"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    androidTestImplementation(platform("androidx.compose:compose-bom:2022.10.00"))

    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

//    implementation(libs.androidx.hilt.navigation.compose)
//    implementation(libs.hilt.android)
//    kapt(libs.hilt.compiler)
//    kapt(libs.hilt.ext.compiler)
}