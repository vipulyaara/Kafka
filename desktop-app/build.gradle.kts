import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("com.kafka.compose")
    id("com.kafka.kotlin.multiplatform")
    alias(libs.plugins.ksp)
}

dependencies {
    kspJvm(libs.kotlininject.compiler)
}

kotlin {
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(projects.base.annotations)
                implementation(projects.base.domain)
                implementation(projects.core.analytics)
                implementation(projects.core.downloader)
                implementation(projects.core.networking)
                implementation(projects.core.play)
                implementation(projects.corePlayback)
                implementation(projects.core.remoteConfig)
                implementation(projects.data.platform)
                implementation(projects.data.prefs)
                implementation(projects.data.repo)
                implementation(projects.domain)
                implementation(projects.navigation)
                implementation(projects.ui.common)
                implementation(projects.ui.downloader)
                implementation(projects.ui.components)
                implementation(projects.ui.homepage)
                implementation(projects.uiPlayback)
                implementation(projects.ui.shared)

                implementation(compose.desktop.currentOs)

                implementation(libs.kotlininject.runtime)
                implementation(libs.kotlin.coroutines.core)
                implementation(libs.kotlin.coroutines.swing)

                implementation(libs.firebase.auth)
                implementation(libs.firebase.firestore)
                implementation(libs.firebase.analytics)

                implementation(libs.androidx.room.runtime)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.kafka.desktop.MainKt"

        nativeDistributions {
            modules("java.sql")
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.kafka"
            packageVersion = "1.0.0"
        }
    }
}

ksp {
    arg("me.tatarka.inject.enableJavaxAnnotations", "true")
}
