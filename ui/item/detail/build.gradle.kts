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
                implementation(projects.core.analytics)
                implementation(projects.core.play)
                implementation(projects.corePlayback)
                implementation(projects.core.remoteConfig)
                implementation(projects.data.repo)
                implementation(projects.domain)
                implementation(projects.navigation)
                implementation(projects.ui.common)
                implementation(projects.ui.components)
                implementation(projects.ui.downloader)
                implementation(projects.uiPlayback)

                implementation(compose.components.resources)
                implementation(compose.material3)
                
                implementation(libs.material.kolor)

                implementation(libs.jetbrains.lifecycle.runtime.compose)
                implementation(libs.jetbrains.lifecycle.viewmodel.compose)
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
    namespace = "com.kafka.item.detail"
}
dependencies {
    implementation(project(":core-playback"))
}
