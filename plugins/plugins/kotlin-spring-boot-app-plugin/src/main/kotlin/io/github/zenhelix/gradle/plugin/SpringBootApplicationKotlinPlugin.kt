package io.github.zenhelix.gradle.plugin

import io.github.zenhelix.gradle.convention.KotlinJvmConventionPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.jetbrains.kotlin.allopen.gradle.AllOpenGradleSubplugin
import org.jetbrains.kotlin.gradle.internal.Kapt3GradleSubplugin
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.noarg.gradle.NoArgGradleSubplugin

public class SpringBootApplicationKotlinPlugin : Plugin<Project> {

    public companion object {
        public const val KOTLIN_SPRING_BOOT_APPLICATION_PLUGIN_ID: String = "io.github.zenhelix.kotlin.spring-boot-app"
    }

    override fun apply(target: Project) {
        with(target.pluginManager) {
            apply(SpringBootApplicationPlugin::class)

            apply(KotlinPluginWrapper::class)

            apply(Kapt3GradleSubplugin::class)

            apply(AllOpenGradleSubplugin::class)
            apply(NoArgGradleSubplugin::class)

            apply(KotlinJvmConventionPlugin::class)
        }
    }
}