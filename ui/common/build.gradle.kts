plugins {
    id("com.android.library")
    id("com.kafka.compose")
    id("com.kafka.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.base.domain)
                api(projects.core.imageLoading)
                implementation(projects.core.networking)
                api(projects.ui.theme)

                api(compose.animation)
                api(compose.foundation)
                api(compose.material3)
                api(compose.components.resources)
                api(compose.runtime)
                api(compose.ui)

                api(libs.jetbrains.adaptive)

                implementation(libs.icons.feather)
                implementation(libs.icons.font.awesome)
                implementation(libs.icons.tabler)

                implementation(libs.kotlinx.atomicfu)
                implementation(libs.kotlin.stdlib)
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
            }
        }
    }
}

android {
    namespace = "com.kafka.common"
}
