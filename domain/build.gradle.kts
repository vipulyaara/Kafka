plugins {
    id("com.android.library")
    id("com.kafka.kotlin.multiplatform")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.corePlayback)
                api(projects.base.domain)
                api(projects.core.analytics)
                api(projects.core.downloader)
                api(projects.core.remoteConfig)
                api(projects.data.repo)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.google.coroutines)

                implementation(libs.google.credentials)
                implementation(libs.google.identity)
                implementation(libs.google.playservices.auth)
            }
        }
    }
}

android {
    namespace = "com.kafka.domain"
}
