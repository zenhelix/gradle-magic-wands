package io.github.zenhelix.gradle.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.tasks.JvmConstants.COMPILE_JAVA_TASK_NAME
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories
import org.springframework.boot.gradle.dsl.SpringBootExtension
import org.springframework.boot.gradle.plugin.SpringBootPlugin

public class SpringBootConventionPlugin : Plugin<Project> {

    public companion object {
        public const val SPRING_BOOT_CONVENTION_PLUGIN_ID: String = "io.github.zenhelix.spring-boot.convention"

        private const val SPRING_BOOT_PLUGIN_NAME = "org.springframework.boot"
        private const val BOOT_BUILD_INFO_TASK_NAME = "bootBuildInfo"
    }

    override fun apply(target: Project) {
        val springBootPluginInClasspath = runCatching { Class.forName("org.springframework.boot.gradle.plugin.SpringBootPlugin") }.isSuccess
        if (springBootPluginInClasspath.not()) {
            return
        }

        val springVersion = SpringBootPlugin.BOM_COORDINATES.replace("org.springframework.boot:spring-boot-dependencies:", "")

        when {
            springVersion.contains("-RC") || springVersion.contains("-M") -> {
                if (!target.repositories.names.contains("spring-milestone")) {
                    target.repositories {
                        //TODO replace by property
                        maven("https://repo.spring.io/milestone") {
                            name = "spring-milestone"
                        }
                    }
                }
            }

            springVersion.contains("-SNAPSHOT")                           -> {
                if (!target.repositories.names.contains("spring-snapshot")) {
                    target.repositories {
                        //TODO replace by property
                        maven("https://repo.spring.io/snapshot") {
                            name = "spring-snapshot"
                        }
                    }
                }
            }
        }

        with(target.pluginManager) {
            withPlugin(SPRING_BOOT_PLUGIN_NAME) {
                if (runCatching { Class.forName("io.github.zenhelix.gradle.convention.SpringConventionPlugin") }.isSuccess) {
                    apply(SpringConventionPlugin::class)
                }
            }
        }

        target.afterEvaluate {
            pluginManager.withPlugin(SPRING_BOOT_PLUGIN_NAME) {
                extensions.configure<SpringBootExtension> {
                    buildInfo()
                }
            }

            if (target.tasks.findByName(COMPILE_JAVA_TASK_NAME) != null && target.tasks.findByName(BOOT_BUILD_INFO_TASK_NAME) != null) {
                target.tasks.named(BOOT_BUILD_INFO_TASK_NAME) {
                    mustRunAfter(target.tasks.named(COMPILE_JAVA_TASK_NAME))
                    dependsOn(target.tasks.named(COMPILE_JAVA_TASK_NAME))
                }
            }
        }
    }

}