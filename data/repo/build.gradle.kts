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

                implementation(libs.firebase.auth)
                implementation(libs.firebase.firestore)
                implementation(libs.kotlin.serialization)

                implementation(libs.kotlininject.runtime)
            }
        }

        val jvmCommon by creating {
            dependsOn(commonMain)
        }

        val jvmMain by getting {
            dependsOn(jvmCommon)
        }

    }
}

android {
    namespace = "com.kafka.data"
}
