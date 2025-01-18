package io.github.zenhelix.gradle.plugin

import io.github.zenhelix.gradle.convention.SpringConventionPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.kotlin.dsl.apply

public class SpringLibraryPlugin : Plugin<Project> {

    public companion object {
        public const val SPRING_LIBRARY_PLUGIN_ID: String = "io.github.zenhelix.spring-library"
    }

    override fun apply(target: Project) {
        with(target.pluginManager) {
            apply(JavaLibraryPlugin::class)
            apply(SpringConventionPlugin::class)
        }
    }

}