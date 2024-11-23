plugins {
    id("com.android.library")
    id("com.kafka.compose")
    id("com.kafka.kotlin.multiplatform")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.base.domain)
                implementation(projects.navigation)
                implementation(projects.ui.components)

                implementation(libs.okio)

                implementation(libs.kotlinx.datetime)
                implementation(libs.ksoup)
                implementation(libs.jetbrains.lifecycle.runtime.compose)
                implementation(libs.jetbrains.lifecycle.viewmodel.compose)

                implementation(libs.kotlin.serialization.proto)
            }
        }
    }
}

android {
    namespace = "com.kafka.reader.core"
}
