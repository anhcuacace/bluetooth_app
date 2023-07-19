
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
//    alias(libs.plugins.hilt) apply false
}
buildscript {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
    dependencies {
        classpath("com.squareup.sqldelight:gradle-plugin:1.5.2")
    }
}