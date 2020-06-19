fun org.gradle.api.publish.maven.MavenPom.addDependencies() = withXml {
    asNode().appendNode("dependencies").let { depNode ->
        configurations.implementation.allDependencies.forEach {
            depNode.appendNode("dependency").apply {
                appendNode("groupId", it.group)
                appendNode("artifactId", it.name)
                appendNode("version", it.version)
            }
        }
    }
}

dependencies {
    implementation(project(Libs.Data.nameDependency))
    implementation(project(Libs.Domain.nameDependency))
    implementation(project(Libs.Language.nameDependency))
    implementation(project(Libs.UiCompose.nameDependency))
    implementation(project(Libs.UiCommon.nameDependency))

    common()
    arch()
    ktx()
    coroutines()

    ui()
    navigation()

    implementation("com.google.android.exoplayer:extension-mediasession:2.9.4")
    implementation(ExoPlayer.player)

    implementation(Hilt.android)
    kapt(Hilt.compiler)
    implementation(Hilt.lifecycle)
    kapt(Hilt.lifecycle_compiler)
}
