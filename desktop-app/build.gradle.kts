import com.kafka.gradle.addKspDependencyForAllTargets
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
        commonMain {
            dependencies {
                implementation(projects.shared.prod)
            }
        }

        jvmMain {
            dependencies {
                implementation(projects.shared.common)
                implementation(projects.shared.prod)

                implementation(compose.desktop.currentOs)

                implementation(libs.kotlininject.runtime)
                implementation(libs.kotlin.coroutines.core)

                implementation(libs.firebase.firestore)
                implementation(libs.firebase.analytics)

                implementation(libs.kotlininject.runtime)

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
        jvmArgs(
            "--add-opens",
            "java.desktop/java.awt.peer=ALL-UNNAMED"
        ) // recommended but not necessary

        if (System.getProperty("os.name").contains("Mac")) {
            jvmArgs("--add-opens", "java.desktop/sun.lwawt=ALL-UNNAMED")
            jvmArgs("--add-opens", "java.desktop/sun.lwawt.macosx=ALL-UNNAMED")
        }
    }
}
