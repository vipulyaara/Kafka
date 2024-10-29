plugins {
    id("com.android.library")
    id("com.kafka.compose")
    id("com.kafka.kotlin.multiplatform")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.base.annotations)
                implementation(projects.data.repo)
                implementation(projects.ui.common)

                implementation(compose.material)

                implementation(libs.kotlin.serialization)
                implementation(libs.jetbrains.navigation.compose)
                implementation(libs.jetbrains.material.navigation)
            }
        }

        val jvmCommon by creating {
            dependsOn(commonMain.get())
        }

        jvmMain {
            dependsOn(jvmCommon)
        }

        androidMain {
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
