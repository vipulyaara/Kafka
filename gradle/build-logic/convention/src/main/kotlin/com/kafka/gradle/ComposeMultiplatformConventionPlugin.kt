package com.kafka.gradle

import com.android.build.gradle.internal.lint.AndroidLintAnalysisTask
import com.android.build.gradle.internal.lint.LintModelWriterTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class ComposeMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.compose")
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

        configureCompose()
    }
}

fun Project.configureCompose() {
    // Workaround for:
    // Task 'generateDebugUnitTestLintModel' uses this output of task
    // 'generateResourceAccessorsForAndroidUnitTest' without declaring an explicit or
    // implicit dependency.
    tasks.matching { it is AndroidLintAnalysisTask || it is LintModelWriterTask }.configureEach {
        mustRunAfter(tasks.matching { it.name.startsWith("generateResourceAccessorsFor") })
    }
}
