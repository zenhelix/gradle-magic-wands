package io.github.zenhelix.gradle.plugin

import io.github.zenhelix.gradle.convention.DesktopComposeConventionPlugin
import io.github.zenhelix.gradle.convention.KotlinMultiplatformComposeConventionPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradleSubplugin

public class MultiplatformComposeLibraryPlugin : Plugin<Project> {

    public companion object {
        public const val KOTLIN_MULTIPLATFORM_COMPOSE_LIBRARY_PLUGIN_ID: String = "io.github.zenhelix.kmm-compose-library"
    }

    override fun apply(target: Project) {
        with(target.pluginManager) {
            apply(MultiplatformLibraryPlugin::class)

            apply(ComposePlugin::class)
            apply(KotlinMultiplatformComposeConventionPlugin::class)
            apply(ComposeCompilerGradleSubplugin::class)
        }

        target.afterEvaluate {
            pluginManager.apply(DesktopComposeConventionPlugin::class)
        }
    }
}