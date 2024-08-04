plugins {
    id("com.kafka.kotlin.multiplatform")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.base.domain)

                implementation(libs.kinject)
                implementation(libs.kotlin.serialization)
                implementation(libs.firebase.remote.config)
            }
        }
    }
}
