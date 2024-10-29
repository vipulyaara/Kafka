plugins {
    id("com.kafka.kotlin.multiplatform")
    alias(libs.plugins.ksp)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.base.annotations)

                api(libs.kermit)
                api(libs.kotlin.coroutines.core)
                api(libs.kotlin.immutable)
                api(libs.kotlin.stdlib)
                api(libs.kotlinx.atomicfu)
                api(libs.kotlininject.runtime)
            }
        }

        jvmMain {
            dependencies {
                api(libs.kotlin.coroutines.swing)
            }
        }

    }
}
