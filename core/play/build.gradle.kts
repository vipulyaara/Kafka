plugins {
    id("com.android.library")
    id("com.kafka.kotlin.multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.base.domain)
                implementation(projects.core.analytics)
            }
        }

        val jvmCommon by creating {
            dependsOn(commonMain.get())
        }

        androidMain {
            dependsOn(jvmCommon)
            dependencies {
                implementation(libs.google.review)
            }
        }
    }
}

android {
    namespace = "com.kafka.core.play"
}
