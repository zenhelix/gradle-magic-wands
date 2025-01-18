package io.github.zenhelix.gradle

import org.gradle.api.artifacts.Configuration

public fun Configuration.hasDependency(group: String, module: String): Boolean = this.allDependencies.any { it.group == group && it.name == module }
