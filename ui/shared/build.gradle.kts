import com.kafka.gradle.addKspDependencyForAllTargets

plugins {
    id("com.android.library")
    id("com.kafka.compose")
    id("com.kafka.kotlin.multiplatform")
    alias(libs.plugins.ksp)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.base.domain)
                implementation(projects.core.analytics)
                implementation(projects.core.networking)
                implementation(projects.core.remoteConfig)
                implementation(projects.data.repo)
                implementation(projects.domain)
                implementation(projects.navigation)
                implementation(projects.ui.common)
                implementation(projects.ui.components)
                implementation(projects.ui.homepage)

                implementation(libs.kotlin.coroutines.swing)
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
    namespace = "com.kafka.shared"
}

addKspDependencyForAllTargets(libs.kotlininject.compiler)
