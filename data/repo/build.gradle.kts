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
                implementation(projects.core.networking)
                implementation(projects.core.remoteConfig)
                api(projects.data.database)
                api(projects.data.models)
                implementation(projects.data.platform)

                implementation(libs.androidx.room.runtime)

                implementation(project.dependencies.platform(libs.openai.kotlin.bom))
                implementation(libs.openai.client)

                implementation(project.dependencies.platform(libs.supabase.bom))
                implementation(libs.supabase.auth)
                implementation(libs.supabase.postgrest)
                implementation(libs.supabase.realtime)
                implementation(libs.supabase.storage)

                implementation(libs.firebase.firestore)
                implementation(libs.kotlin.serialization)

                api(libs.ktor.client.core)
                api(libs.ktor.client.contentnegotiation)
                api(libs.ktor.client.java)
                api(libs.ktor.client.logging)
                api(libs.ktor.serialization)

                implementation(libs.kotlininject.runtime)
            }
        }

        val jvmCommon by creating {
            dependsOn(commonMain)
        }

        val jvmMain by getting {
            dependsOn(jvmCommon)

            dependencies {
                implementation(libs.ktor.client.cio)
            }
        }

        val androidMain by getting {
            dependsOn(jvmMain)
        }
    }
}

android {
    namespace = "com.kafka.data"
}
