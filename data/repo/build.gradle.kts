plugins {
    id("com.android.library")
    id("com.kafka.kotlin.multiplatform")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.base.domain)
                api(projects.core.networking)
                api(projects.core.remoteConfig)
                api(projects.data.database)
                api(projects.data.models)
                api(projects.data.platform)

                api(libs.androidx.room.runtime)

                api(project.dependencies.platform(libs.openai.kotlin.bom))
                api(libs.openai.client)
                api(libs.ktor.client.contentnegotiation)

                api(project.dependencies.platform(libs.supabase.bom))
                api(libs.supabase.auth)
                api(libs.supabase.postgrest)
                api(libs.supabase.realtime)
                api(libs.supabase.storage)

                api(libs.firebase.auth)
                api(libs.firebase.firestore)
                api(libs.kotlin.serialization)

                api(libs.ktor.client.cio)
            }
        }

        val jvmCommon by creating {
            dependsOn(commonMain.get())
        }

        androidMain {
            dependsOn(jvmCommon)
        }
    }
}

android {
    namespace = "com.kafka.data"
}
