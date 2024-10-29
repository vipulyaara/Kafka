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
                implementation(projects.core.analytics)
                implementation(projects.core.downloader)
                implementation(projects.data.repo)
                implementation(projects.domain)
                implementation(projects.navigation)
                implementation(projects.ui.common)
                implementation(projects.ui.components)

                implementation(compose.components.resources)
                implementation(compose.material3)

                implementation(libs.ksoup)
                implementation(libs.jetbrains.lifecycle.runtime.compose)
                implementation(libs.jetbrains.lifecycle.viewmodel.compose)

                implementation(libs.kotlin.serialization.proto)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.jsoup)
            }
        }
    }
}

android {
    namespace = "com.kafka.reader.epub"
}

