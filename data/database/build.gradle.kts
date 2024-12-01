import com.kafka.gradle.addKspDependencyForAllTargets

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

kotlin {
    sourceSets {
        commonMain {
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

                api(libs.ksoup)
                api(libs.kotlinx.datetime)
            }
        }
    }
}

android {
    namespace = "com.kafka.database"
}

addKspDependencyForAllTargets(libs.androidx.room.compiler)