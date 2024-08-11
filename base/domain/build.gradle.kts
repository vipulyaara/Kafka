plugins {
    id("com.kafka.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.kinject)
                api(libs.kotlin.coroutines.core)
                api(libs.kotlin.stdlib)
                api(libs.kermit)
                api(libs.kotlin.immutable)
                api(libs.kotlinx.atomicfu)
            }
        }

        val jvmMain by getting
    }
}
