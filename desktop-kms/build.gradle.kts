import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("com.kafka.compose")
    id("com.kafka.kotlin.multiplatform")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.buildConfig)
}

buildConfig {
    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())

    buildConfigField(
        "String",
        "SUPABASE_ADMIN_KEY",
        properties["SUPABASE_ADMIN_KEY"]?.toString()
            ?: System.getenv("SUPABASE_ADMIN_KEY")
    )
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
                implementation(compose.components.resources)

                implementation(libs.kotlininject.runtime)
                implementation(libs.kotlin.coroutines.core)

                implementation(libs.firebase.firestore)
                implementation(libs.firebase.analytics)
                implementation(libs.firebase.storage)

                implementation(libs.kotlininject.runtime)

                implementation(libs.androidx.room.runtime)
                implementation(libs.androidx.lifecycle.runtime)

                implementation(libs.jetbrains.navigation.compose)
                implementation(libs.jetbrains.material.navigation)

                implementation(libs.icons.tabler)
                implementation(libs.icons.feather)
                implementation(libs.icons.font.awesome)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.kafka.kms.main.MainKt"

        nativeDistributions {
            modules("java.sql")
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.kafka.kms"
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
