plugins {
    id("com.kafka.kotlin.multiplatform")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.base.domain)
                api(libs.kotlin.serialization)
                implementation(libs.firestore.ktx)
            }
        }
    }
}
