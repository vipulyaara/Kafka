plugins {
    id("com.android.library")
    id("com.kafka.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.base.domain)
                implementation(projects.data.platform)

                implementation(libs.kotlininject.runtime)

                implementation(libs.firebase.analytics)

                implementation(project.dependencies.platform(libs.supabase.bom))
                implementation(libs.supabase.auth)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.firebase.crashlytics)
                implementation(libs.mixpanel)
            }
        }
    }
}

android {
    namespace = "com.kafka.analytics"
}
