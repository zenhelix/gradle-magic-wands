package io.github.zenhelix.gradle.plugin

import io.github.zenhelix.gradle.convention.DesktopComposeConventionPlugin
import io.github.zenhelix.gradle.convention.KotlinMultiplatformComposeConventionPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.compose.desktop.DesktopExtension
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradleSubplugin

public class MultiplatformComposeAppPlugin : Plugin<Project> {
    public companion object {
        public const val MULTIPLATFORM_COMPOSE_APP_PLUGIN_ID: String = "io.github.zenhelix.kmm-compose-app"
    }

    override fun apply(target: Project) {
        with(target.pluginManager) {
            apply(MultiplatformAppPlugin::class)

            apply(ComposePlugin::class)
            apply(KotlinMultiplatformComposeConventionPlugin::class)
            apply(ComposeCompilerGradleSubplugin::class)
        }

        target.extensions.configure<ComposeExtension> {
            this.extensions.configure<DesktopExtension> {
                application {
                    nativeDistributions {
                        targetFormats(
                            TargetFormat.Dmg, TargetFormat.Pkg,
                            TargetFormat.Msi, TargetFormat.Exe,
                            TargetFormat.Deb, TargetFormat.Rpm
                        )
                    }
                }
            }
        }
        target.afterEvaluate {
            pluginManager.apply(DesktopComposeConventionPlugin::class)
        }
    }
}