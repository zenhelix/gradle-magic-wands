package io.github.zenhelix.gradle.convention

import io.github.zenhelix.gradle.convention.ExtraKotlinJvmExtension.Companion.DEFAULT_ENABLED_EXTRA_KOTLIN_JVM_PLUGIN
import io.github.zenhelix.gradle.convention.ExtraKotlinJvmExtension.Companion.EXTRA_KOTLIN_JVM_EXTENSION_NAME
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

/**
 * @see [-Xemit-jvm-type-annotations](https://kotlinlang.org/docs/whatsnew14.html#type-annotations-in-the-jvm-bytecode)
 *
 * [-Xemit-jvm-type-annotations] Necessary for annotations in generics to get into the bytecode, for example List<@Min(0) @Max(10) Int>
 *
 * Appeared in version 1.4 and became the default after version 1.6
 * @see [1.6](https://kotlinlang.org/docs/whatsnew16.html#support-for-annotations-on-class-type-parameters)
 */
public class KotlinJvmConventionPlugin : Plugin<Project> {

    public companion object {
        public const val KOTLIN_JVM_CONVENTION_PLUGIN_ID: String = "io.github.zenhelix.kotlin-jvm.convention"

        private const val KOTLIN_JVM_PLUGIN_NAME: String = "org.jetbrains.kotlin.jvm"
    }

    override fun apply(target: Project) {
        val extraKotlinJvmExtension = target.extensions.create<ExtraKotlinJvmExtension>(EXTRA_KOTLIN_JVM_EXTENSION_NAME)

        target.afterEvaluate {
            val enabled = extraKotlinJvmExtension.enabled.getOrElse(DEFAULT_ENABLED_EXTRA_KOTLIN_JVM_PLUGIN)
            if (enabled.not()) {
                return@afterEvaluate
            }

            pluginManager.withPlugin(KOTLIN_JVM_PLUGIN_NAME) {
                val kotlinPluginInClasspath = runCatching { Class.forName("org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile") }.isSuccess
                if (kotlinPluginInClasspath) {
                    tasks.withType<KotlinJvmCompile>().configureEach {
                        compilerOptions {
                            // Especially through the creation, because sometimes freeCompilerArgs can be null
                            freeCompilerArgs.set(
                                freeCompilerArgs.getOrElse(emptyList()).toMutableList().apply {
                                    add("-Xemit-jvm-type-annotations")
                                }
                            )
                        }
                    }

                    extensions.findByType<KaptExtension>()?.also {
                        val annotationProcessorConfiguration = configurations.findByName("annotationProcessor")
                        val kaptProcessorConfiguration = configurations.findByName("kapt")
                        if (annotationProcessorConfiguration != null && kaptProcessorConfiguration != null) {
                            kaptProcessorConfiguration.extendsFrom(annotationProcessorConfiguration)
                        }

                        extensions.configure<KaptExtension> {
                            useBuildCache = true
                        }
                    }
                }
            }
        }
    }
}