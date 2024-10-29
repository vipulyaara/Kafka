plugins {
    id("com.kafka.kotlin.multiplatform")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.base.domain)
                api(libs.kotlin.serialization)
                implementation(libs.firebase.firestore)
            }
        }
    }
}
