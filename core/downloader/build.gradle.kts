plugins {
    id("com.android.library")
    id("com.kafka.kotlin.multiplatform")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.base.domain)
                implementation(projects.core.analytics)
                implementation(projects.core.remoteConfig)
                implementation(projects.data.repo)
                implementation(projects.ui.common)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.documentfile)
                implementation(libs.kotlininject.runtime)
                implementation(libs.threeTenAbp)
            }
        }
    }
}

android {
    namespace = "com.kafka.downloader.kt"
}
