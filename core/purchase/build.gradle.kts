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

                implementation(libs.purchase.core)
                implementation(libs.purchase.datetime)
                implementation(libs.purchase.result)
            }
        }
    }
}

android {
    namespace = "com.kafka.purchase"
}
