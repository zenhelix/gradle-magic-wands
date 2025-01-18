package io.github.zenhelix.gradle.plugin

import io.github.zenhelix.gradle.convention.SpringConventionPlugin
import io.github.zenhelix.gradle.hasDependency
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin.ANNOTATION_PROCESSOR_CONFIGURATION_NAME
import org.gradle.api.plugins.JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME
import org.gradle.kotlin.dsl.apply

public class SpringBootAutoconfigureLibraryPlugin : Plugin<Project> {

    public companion object {
        public const val SPRING_BOOT_AUTOCONFIGURE_LIBRARY_PLUGIN_ID: String = "io.github.zenhelix.spring-boot-autoconfigure-library"
    }

    override fun apply(target: Project) {
        with(target.pluginManager) {
            apply(SpringLibraryPlugin::class)
            apply(SpringConventionPlugin::class)
        }

        with(target) {
            dependencies.apply {
                if (!hasDependency(IMPLEMENTATION_CONFIGURATION_NAME, "org.springframework.boot", "spring-boot-autoconfigure")) {
                    add(IMPLEMENTATION_CONFIGURATION_NAME, "org.springframework.boot:spring-boot-autoconfigure")
                }

                if (pluginManager.hasPlugin("kotlin-kapt")) {
                    if (!hasDependency("kapt", "org.springframework.boot", "spring-boot-configuration-processor")) {
                        add("kapt", "org.springframework.boot:spring-boot-configuration-processor")
                    }
                    if (!hasDependency("kapt", "org.springframework.boot", "spring-boot-autoconfigure-processor")) {
                        add("kapt", "org.springframework.boot:spring-boot-autoconfigure-processor")
                    }
                } else {
                    if (!hasDependency(ANNOTATION_PROCESSOR_CONFIGURATION_NAME, "org.springframework.boot", "spring-boot-configuration-processor")) {
                        add(ANNOTATION_PROCESSOR_CONFIGURATION_NAME, "org.springframework.boot:spring-boot-configuration-processor")
                    }
                    if (!hasDependency(ANNOTATION_PROCESSOR_CONFIGURATION_NAME, "org.springframework.boot", "spring-boot-autoconfigure-processor")) {
                        add(ANNOTATION_PROCESSOR_CONFIGURATION_NAME, "org.springframework.boot:spring-boot-autoconfigure-processor")
                    }
                }
            }
        }

    }

}