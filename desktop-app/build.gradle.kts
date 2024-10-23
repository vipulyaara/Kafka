import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("com.kafka.compose")
    id("com.kafka.kotlin.multiplatform")
    alias(libs.plugins.ksp)
}

dependencies {
    kspJvm(libs.kotlininject.compiler)
    kspJvm(libs.androidx.room.compiler)
}

kotlin {
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(projects.base.annotations)
                implementation(projects.base.domain)
                implementation(projects.core.analytics)
                implementation(projects.core.downloaderKt)
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
                implementation(projects.ui.components)
                implementation(projects.ui.homepage)
                implementation(projects.uiPlayback)
                implementation(projects.ui.reader.epub)
                implementation(projects.ui.shared)
                implementation(projects.ui.webview)

                implementation(compose.desktop.currentOs)

                implementation(libs.kotlininject.runtime)
                implementation(libs.kotlin.coroutines.core)
                implementation(libs.kotlin.coroutines.swing)

                implementation(libs.firebase.auth)
                implementation(libs.firebase.firestore)
                implementation(libs.firebase.analytics)

                implementation(libs.androidx.room.runtime)
                implementation(libs.androidx.lifecycle.runtime)

                implementation(libs.jetbrains.navigation.compose)
                implementation(libs.jetbrains.material.navigation)
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
            packageName = "com.kafka.user"
            packageVersion = "1.0.0"
        }

        buildTypes.release.proguard {
            configurationFiles.from("desktop-proguard-rules.pro")
        }

        jvmArgs("--add-opens", "java.desktop/sun.awt=ALL-UNNAMED")
        jvmArgs("--add-opens", "java.desktop/java.awt.peer=ALL-UNNAMED") // recommended but not necessary

        if (System.getProperty("os.name").contains("Mac")) {
            jvmArgs("--add-opens", "java.desktop/sun.lwawt=ALL-UNNAMED")
            jvmArgs("--add-opens", "java.desktop/sun.lwawt.macosx=ALL-UNNAMED")
        }
    }
}

ksp {
    arg("me.tatarka.inject.enableJavaxAnnotations", "true")
}
