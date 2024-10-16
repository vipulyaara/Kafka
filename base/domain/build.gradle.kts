plugins {
    id("com.kafka.kotlin.multiplatform")
    alias(libs.plugins.ksp)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.base.annotations)

                api(libs.kotlin.coroutines.swing)

                api(libs.kinject)
                api(libs.kotlin.coroutines.core)
                api(libs.kotlin.stdlib)
                api(libs.kermit)
                api(libs.kotlin.immutable)
                api(libs.kotlinx.atomicfu)
                api(libs.kotlininject.runtime)
            }
        }

        val jvmMain by getting
    }
}
