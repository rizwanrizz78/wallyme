plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.hiltAndroid) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.gmsGoogleServices) apply false
}

buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.1")
    }
}
