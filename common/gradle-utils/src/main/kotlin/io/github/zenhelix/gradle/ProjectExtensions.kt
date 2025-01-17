package io.github.zenhelix.gradle

import org.gradle.api.Project

public fun Project.hasDependency(scope: String, group: String, module: String): Boolean =
    configurations.findByName(scope)?.hasDependency(group = group, module = module) == true

public fun Project.hasConfiguration(configurationName: String): Boolean = configurations.findByName(configurationName) != null
