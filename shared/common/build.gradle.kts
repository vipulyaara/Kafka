import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("com.android.library")
    id("com.kafka.compose")
    id("com.kafka.kotlin.multiplatform")
    id("org.jetbrains.kotlin.native.cocoapods")
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
        "SUPABASE_ADMIN_KEY",
        properties["SUPABASE_ADMIN_KEY"]?.toString()
            ?: System.getenv("SUPABASE_ADMIN_KEY")
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
    cocoapods {
        summary = "Some description for the appLauncher Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "16.0"
        podfile = project.file("../../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(projects.base.domain)
                api(projects.core.analytics)
                api(projects.core.downloader)
                api(projects.core.networking)
                api(projects.core.play)
                api(projects.corePlayback)
                api(projects.core.reader)
                api(projects.core.remoteConfig)
                api(projects.data.platform)
                api(projects.data.repo)
                api(projects.domain)
                api(projects.navigation)

                api(projects.ui.auth)
                api(projects.ui.common)
                api(projects.ui.components)
                api(projects.ui.homepage)
                api(projects.ui.item.detail)
                api(projects.ui.library)
                api(projects.uiPlayback)
                api(projects.ui.profile)
                api(projects.ui.reader.epub)
                api(projects.ui.root)
                api(projects.ui.search)
                api(projects.ui.summary)
                api(projects.ui.theme)
                api(projects.ui.webview)

                api(libs.jetbrains.navigation.compose)
            }
        }

        val jvmCommon by creating {
            dependsOn(commonMain.get())
        }

        jvmMain {
            dependsOn(jvmCommon)
            dependencies {
                implementation(libs.kotlin.coroutines.swing)
            }
        }

        androidMain {
            dependsOn(jvmCommon)
            dependencies {
                implementation(libs.androidx.lifecycle.process)
                implementation(libs.kermit)
                implementation(libs.kermit.crashlytics)
                implementation(libs.okhttp.okhttp)
            }
        }
    }
}

android {
    namespace = "com.kafka.shared.common"
}
