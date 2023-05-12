import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.cacheFixPlugin)
    alias(libs.plugins.hilt)
    id("kotlin-kapt")
}

android {
    namespace = "com.kafka.remote.config"
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}
