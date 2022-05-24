package com.roadrunner.gradle.extentions

import org.gradle.api.artifacts.VersionCatalog

fun VersionCatalog.getDep(alias: String): String {
    val dep = findLibrary(alias).get().get()
    return "${dep.module.group}:${dep.module.name}:${dep.versionConstraint.displayName}"
}
