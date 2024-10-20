plugins {
    id("com.android.library")
    id("com.kafka.compose")
    id("com.kafka.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.base.annotations)
                implementation(projects.base.domain)

                api(libs.coil3.coil)
                api(libs.coil3.compose)
                api(libs.coil3.network)
                api(libs.coil3.svg)

                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.runtime)
                implementation(compose.ui)

                api(libs.kotlin.coroutines.swing)
                implementation(libs.kotlininject.runtime)
            }
        }

        val jvmCommon by creating {
            dependsOn(commonMain)
        }

        val jvmMain by getting {
            dependsOn(jvmCommon)
        }

        val androidMain by getting {
            dependsOn(jvmCommon)

            dependencies {
                implementation(libs.coil.coil)
                implementation(libs.coil.compose)
            }
        }
    }
}

android {
    namespace = "com.kafka.image"
}
