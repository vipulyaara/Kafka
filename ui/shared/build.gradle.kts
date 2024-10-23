import com.kafka.gradle.addKspDependencyForAllTargets
import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("com.android.library")
    id("com.kafka.compose")
    id("com.kafka.kotlin.multiplatform")
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.ksp)
}

buildConfig {
    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())

    buildConfigField(
        "String",
        "GOOGLE_SERVER_CLIENT_ID",
        properties["GOOGLE_SERVER_CLIENT_ID"]?.toString()
            ?: System.getenv("GOOGLE_SERVER_CLIENT_ID")
    )

    buildConfigField(
        "String",
        "SUPABASE_PROJECT_URL",
        properties["SUPABASE_PROJECT_URL"]?.toString()
            ?: System.getenv("SUPABASE_PROJECT_URL")
    )

    buildConfigField(
        "String",
        "SUPABASE_KEY",
        properties["SUPABASE_KEY"]?.toString()
            ?: System.getenv("SUPABASE_KEY")
    )

    buildConfigField(
        "String",
        "MIXPANEL_TOKEN",
        properties["MIXPANEL_TOKEN"]?.toString()
            ?: System.getenv("MIXPANEL_TOKEN")
    )
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.base.domain)
                implementation(projects.core.analytics)
                implementation(projects.core.downloaderKt)
                implementation(projects.core.networking)
                implementation(projects.core.play)
                implementation(projects.corePlayback)
                implementation(projects.core.remoteConfig)
                implementation(projects.data.platform)
                implementation(projects.data.repo)
                implementation(projects.domain)
                implementation(projects.navigation)
                implementation(projects.ui.auth)
                implementation(projects.ui.common)
                implementation(projects.ui.components)
                implementation(projects.ui.homepage)
                implementation(projects.ui.item.detail)
                implementation(projects.ui.library)
                implementation(projects.uiPlayback)
                implementation(projects.ui.profile)
                implementation(projects.ui.reader.epub)
                implementation(projects.ui.search)
                implementation(projects.ui.summary)
                implementation(projects.ui.theme)
                implementation(projects.ui.webview)

                implementation(compose.components.resources)

                implementation(libs.firebase.auth)
                implementation(libs.firebase.firestore)
                implementation(libs.kotlin.coroutines.swing)
                implementation(libs.kotlininject.runtime)
                implementation(libs.threeTenAbp)

                implementation(libs.jetbrains.lifecycle.runtime.compose)
                implementation(libs.jetbrains.lifecycle.viewmodel.compose)
                implementation(libs.jetbrains.navigation.compose)
                implementation(libs.jetbrains.material.navigation)

                implementation(libs.androidx.room.runtime)

                implementation(project.dependencies.platform(libs.supabase.bom))
                implementation(libs.supabase.auth)
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
                implementation(libs.accompanist.permissions)
                implementation(libs.androidx.lifecycle.process)
                implementation(libs.firebase.common)
                implementation(libs.kermit)
                implementation(libs.kermit.crashlytics)
                implementation(libs.threeTenAbp)
                implementation(libs.fetch)
                implementation(libs.okhttp.okhttp)
            }
        }
    }
}

ksp {
    arg("me.tatarka.inject.enableJavaxAnnotations", "true")
}

android {
    namespace = "com.kafka.shared"
}

addKspDependencyForAllTargets(libs.kotlininject.compiler)
