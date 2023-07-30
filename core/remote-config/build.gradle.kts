@Suppress("DSL_SCOPE_VIOLATION") // Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.cacheFixPlugin)
    alias(libs.plugins.hilt)
    id("kotlin-kapt")
    id("kotlinx-serialization")
}

android {
    namespace = "com.kafka.remote.config"
}

dependencies {
    implementation(project(":core:analytics"))
    implementation(project(":base:domain"))

    implementation(libs.core.ktx)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.kotlin.serialization)

    implementation(platform(libs.google.bom))
    implementation(libs.google.remoteConfig)
}
