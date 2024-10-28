plugins {
    id("com.kafka.kotlin.multiplatform")
    alias(libs.plugins.ksp)
}

kotlin {
    sourceSets {
        val commonMain by getting {
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

        val jvmCommon by creating {
            dependsOn(commonMain)
        }

        val jvmMain by getting {
            dependsOn(jvmCommon)

            dependencies {
                api(libs.kotlin.coroutines.swing)
            }
        }

    }
}
