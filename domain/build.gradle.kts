plugins {
    id("com.android.library")
    id("com.kafka.kotlin.multiplatform")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.corePlayback)
                implementation(projects.base.domain)
                implementation(projects.core.analytics)
                implementation(projects.core.downloaderKt)

                implementation(projects.core.remoteConfig)
                implementation(projects.data.repo)

                implementation(libs.androidx.room.runtime)

                implementation(libs.firebase.firestore)

                implementation(libs.kotlininject.runtime)

                implementation(project.dependencies.platform(libs.supabase.bom))
                implementation(libs.supabase.auth.compose)
                implementation(libs.supabase.postgrest)
                implementation(libs.supabase.realtime)
                implementation(libs.supabase.storage)
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
