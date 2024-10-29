plugins {
    id("com.kafka.kotlin.multiplatform")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.base.domain)

                implementation(libs.firebase.remote.config)
                implementation(libs.kotlin.serialization)
            }
        }
    }
}
