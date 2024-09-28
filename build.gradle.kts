import com.android.build.gradle.BaseExtension
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.lint) apply false
    alias(libs.plugins.cacheFixPlugin) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose.compiler) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.gms.googleServices) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.androidTest) apply false
    alias(libs.plugins.androidx.baselineprofile) apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        jcenter()

        maven(url = "https://jitpack.io")
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
        maven(url = "https://androidx.dev/snapshots/latest/artifacts/repository")
        maven(url = "https://repo.repsy.io/mvn/chrynan/public")
    }


    apply(from = rootProject.file("spotless/spotless.gradle"))

    // Configure Java to use our chosen language level. Kotlin will automatically
    // pick this up
    plugins.withType<JavaBasePlugin>().configureEach {
        extensions.configure<JavaPluginExtension> {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(17))
            }
        }
    }

    tasks.withType<KotlinCompilationTask<*>>().configureEach {
        compilerOptions {
            // Treat all Kotlin warnings as errors
            allWarningsAsErrors.set(false)

            // Enable experimental coroutines APIs, including Flow
            freeCompilerArgs.addAll(
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.FlowPreview",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.FlowPreview",
                "-opt-in=kotlin.Experimental",
                "-opt-in=androidx.compose.material3.ExperimentalMaterialApi",
                "-opt-in=coil.annotation.ExperimentalCoilApi",
                "-opt-in=androidx.compose.foundation.text.InternalFoundationTextApi",
                "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
                "-opt-in=androidx.compose.ui.unit.ExperimentalUnitApi",
                "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
                "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
                "-opt-in=androidx.lifecycle.compose.ExperimentalLifecycleComposeApi",
                "-opt-in=kotlin.ExperimentalStdlibApi",
                "-opt-in=com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi",
                "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                "-opt-in=kotlin.annotation.AnnotationTarget.ExperimentalTime",
                "-opt-in=kotlin.RequiresOptIn",
                "-opt-in=androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi",
                "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
                "-opt-in=androidx.compose.ui.text.ExperimentalTextApi",
                "-opt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi"
            )

            if (project.hasProperty("tivi.enableComposeCompilerReports")) {
                freeCompilerArgs.addAll(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
                            project.buildDir.absolutePath + "/compose_metrics",
                )
                freeCompilerArgs.addAll(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
                            project.buildDir.absolutePath + "/compose_metrics",
                )
            }
        }
    }

    // Configure kapt
    pluginManager.withPlugin(rootProject.libs.plugins.kotlin.kapt.get().pluginId) {
        extensions.getByType<KaptExtension>().correctErrorTypes = true
    }

    // Configure Android projects
    pluginManager.withPlugin("com.android.application") {
        configureAndroidProject()
    }
    pluginManager.withPlugin("com.android.library") {
        configureAndroidProject()
    }
    pluginManager.withPlugin("com.android.test") {
        configureAndroidProject()
    }
}

fun Project.configureAndroidProject() {
    extensions.configure<BaseExtension> {
        compileSdkVersion(libs.versions.compileSdk.get().toInt())

//        apply(plugin = "com.squareup.sort-dependencies")

        defaultConfig {
            minSdk = libs.versions.minSdk.get().toInt()
            targetSdk = libs.versions.targetSdk.get().toInt()
        }

        // Can remove this once https://issuetracker.google.com/issues/260059413 is fixed.
        // See https://kotlinlang.org/docs/gradle-configure-project.html#gradle-java-toolchains-support
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }
}
