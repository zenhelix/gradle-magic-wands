package io.github.zenhelix.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.plugins.JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME
import org.gradle.kotlin.dsl.apply

public class SpringBootStarterLibraryPlugin : Plugin<Project> {

    public companion object {
        public const val SPRING_BOOT_STARTER_LIBRARY_PLUGIN_ID: String = "io.github.zenhelix.spring-boot-starter"
    }

    override fun apply(target: Project) {
        with(target.pluginManager) {
            apply(SpringBootAutoconfigureLibraryPlugin::class)
        }

        with(target) {
            dependencies.apply {
                if (!hasDependency(IMPLEMENTATION_CONFIGURATION_NAME, "org.springframework.boot", "spring-boot-starter")) {
                    add(IMPLEMENTATION_CONFIGURATION_NAME, "org.springframework.boot:spring-boot-starter")
                }
            }
        }

    }

    private fun Project.hasDependency(scope: String, group: String, module: String): Boolean =
        configurations.findByName(scope)?.hasDependency(group = group, module = module) == true

    private fun Configuration.hasDependency(group: String, module: String): Boolean = this.allDependencies.any { it.group == group && it.name == module }

}