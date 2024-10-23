plugins {
    id("com.android.library")
    id("com.kafka.kotlin.multiplatform")
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    kspAndroid(libs.androidx.room.compiler)
    kspJvm(libs.androidx.room.compiler)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.base.domain)
                api(projects.data.models)
                api(projects.data.platform)
                api(projects.data.prefs)

                implementation(libs.kotlin.serialization)
                implementation(libs.firebase.firestore)

                implementation(libs.androidx.room.runtime)
                implementation(libs.androidx.sqlite.bundled)
                implementation(libs.kotlininject.runtime)
                implementation(libs.threeTenAbp)

                api(libs.jsoup)
                api(libs.kotlinx.datetime)
            }
        }
    }
}

android {
    namespace = "com.kafka.database"
}
