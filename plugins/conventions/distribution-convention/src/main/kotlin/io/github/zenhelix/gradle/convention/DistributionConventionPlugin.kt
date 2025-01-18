package io.github.zenhelix.gradle.convention

import io.github.zenhelix.gradle.convention.DistributionExtension.Companion.DISTRIBUTION_TASK_GROUP_NAME
import io.github.zenhelix.gradle.convention.DistributionExtension.Companion.EXTRA_DISTRIBUTION_EXTENSION_NAME
import io.github.zenhelix.gradle.convention.utils.DistributionFlavour
import io.github.zenhelix.gradle.convention.utils.DistributionName
import io.github.zenhelix.gradle.convention.utils.lowerCamelCaseName
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.bundling.Zip
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.desktop.DesktopExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalDistributionDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsBinaryMode
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrSubTarget.Companion.DISTRIBUTION_TASK_NAME
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrTarget

@OptIn(ExperimentalDistributionDsl::class)
public class DistributionConventionPlugin : Plugin<Project> {

    public companion object {
        public const val DISTRIBUTION_CONVENTION_PLUGIN_NAME: String = "io.github.zenhelix.distribution.convention"

        private const val KOTLIN_MULTIPLATFORM_PLUGIN_NAME = "org.jetbrains.kotlin.multiplatform"
        internal const val KOTLIN_COMPOSE_PLUGIN_NAME = "org.jetbrains.compose"
    }

    override fun apply(target: Project) {
        val distributionExtension =
            target.extensions.findByType<DistributionExtension>() ?: target.extensions.create<DistributionExtension>(EXTRA_DISTRIBUTION_EXTENSION_NAME)

        val kotlinKmmPluginInClasspath = runCatching { Class.forName("org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension") }.isSuccess
        if (kotlinKmmPluginInClasspath.not()) {
            return
        }

        target.pluginManager.withPlugin(KOTLIN_MULTIPLATFORM_PLUGIN_NAME) {
            target.extensions.configure<KotlinMultiplatformExtension> {
                targets.withType<KotlinJsIrTarget> {
                    whenBrowserConfigured {
                        distribution {
                            outputDirectory.set(
                                target.layout.buildDirectory
                                    .dir(distributionExtension.outputDestination)
                                    .map { it.dir(this@withType.targetName) }
                            )
                        }
                    }
                }
            }
        }

        target.afterEvaluate {
            val version = this@afterEvaluate.version.toString().takeIf { it != Project.DEFAULT_VERSION }

            pluginManager.withPlugin(KOTLIN_MULTIPLATFORM_PLUGIN_NAME) {
                extensions.configure<KotlinMultiplatformExtension> {
                    targets.withType<KotlinJsIrTarget>()
                        .flatMap { it.binaries }
                        .forEach { binary ->
                            val moduleName = binary.target.moduleName ?: binary.compilation.compilationName
                            val targetName = binary.target.targetName
                            val binaryName =
                                if (binary.mode == KotlinJsBinaryMode.PRODUCTION && binary.compilation.name == KotlinCompilation.MAIN_COMPILATION_NAME) {
                                    ""
                                } else {
                                    binary.name
                                }
                            val subTargetName = if (binary.target.isBrowserConfigured) {
                                "browser"
                            } else if (binary.target.isD8Configured) {
                                "d8"
                            } else if (binary.target.isNodejsConfigured) {
                                "node"
                            } else {
                                throw IllegalStateException("Not supported ${binary.target.targetName}")
                            }

                            val distributionTask = tasks.named<Copy>(lowerCamelCaseName(targetName, subTargetName, binaryName, DISTRIBUTION_TASK_NAME))

                            val zipTask = tasks.register<Zip>(lowerCamelCaseName("zip", targetName, subTargetName, binaryName, DISTRIBUTION_TASK_NAME)) {
                                group = DISTRIBUTION_TASK_GROUP_NAME

                                from(binary.distribution.outputDirectory)
                                destinationDirectory.set(
                                    layout.buildDirectory
                                        .dir(distributionExtension.finalOutputDestination)
                                        .map { it.dir("web") }
                                )
                                archiveFileName.set(
                                    DistributionName.web(
                                        moduleName = moduleName,
                                        version = version,
                                        flavour = binary.mode.toDistributionName(),
                                        target = binary.target
                                    )
                                )
                            }

                            distributionTask.get().finalizedBy(zipTask)
                        }
                }

            }

        }

        val kotlinComposePluginInClasspath = runCatching { Class.forName("org.jetbrains.compose.ComposeExtension") }.isSuccess
        if (kotlinComposePluginInClasspath) {
            target.pluginManager.withPlugin(KOTLIN_COMPOSE_PLUGIN_NAME) {
                target.extensions.configure<ComposeExtension> {
                    this.extensions.configure<DesktopExtension> {
                        application {
                            nativeDistributions {
                                outputBaseDir.set(target.layout.buildDirectory.dir(distributionExtension.outputDestination))
                            }
                        }
                    }
                }
            }
        }
    }

    private fun KotlinJsBinaryMode.toDistributionName() = when (this) {
        KotlinJsBinaryMode.PRODUCTION -> DistributionFlavour.RELEASE
        KotlinJsBinaryMode.DEVELOPMENT -> DistributionFlavour.DEBUG
    }

}
