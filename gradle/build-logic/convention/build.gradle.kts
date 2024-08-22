plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.spotless.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.composeCompiler.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("kotlinMultiplatform") {
            id = "com.kafka.kotlin.multiplatform"
            implementationClass = "com.kafka.gradle.KotlinMultiplatformConventionPlugin"
        }

        register("kotlinAndroid") {
            id = "com.kafka.kotlin.android"
            implementationClass = "com.kafka.gradle.KotlinAndroidConventionPlugin"
        }

        register("androidApplication") {
            id = "com.kafka.android.application"
            implementationClass = "com.kafka.gradle.AndroidApplicationConventionPlugin"
        }

        register("androidLibrary") {
            id = "com.kafka.android.library"
            implementationClass = "com.kafka.gradle.AndroidLibraryConventionPlugin"
        }

        register("compose") {
            id = "com.kafka.compose"
            implementationClass = "com.kafka.gradle.ComposeMultiplatformConventionPlugin"
        }
    }
}
