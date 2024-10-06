plugins {
    id("com.android.library")
    id("com.kafka.compose")
    id("com.kafka.kotlin.multiplatform")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.base.annotations)
                implementation(projects.data.repo)
                implementation(projects.ui.common)

                implementation(compose.material)

                implementation(libs.kotlin.serialization)
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
                api(libs.androidx.navigation.compose)
                implementation(libs.compose.material.navigation)
            }
        }
    }
}

android {
    namespace = "com.kafka.navigation"
}
