plugins {
    id("com.android.library")
    id("com.kafka.kotlin.multiplatform")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.base.domain)
                implementation(projects.core.analytics)
                implementation(projects.core.remoteConfig)
                implementation(projects.data.repo)
                implementation(projects.ui.common)
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
                implementation(libs.androidx.documentfile)
                implementation(libs.fetch)
                implementation(libs.kotlininject.runtime)
                implementation(libs.threeTenAbp)
            }
        }
    }
}

android {
    namespace = "com.kafka.downloader.kt"
}
