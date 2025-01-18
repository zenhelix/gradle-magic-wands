package io.github.zenhelix.gradle.convention

import io.github.zenhelix.gradle.convention.ExtraLombokExtension.Companion.DEFAULT_ENABLED_EXTRA_LOMBOK_PLUGIN
import io.github.zenhelix.gradle.convention.ExtraLombokExtension.Companion.EXTRA_LOMBOK_EXTENSION_NAME
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import org.jetbrains.kotlin.lombok.gradle.LombokSubplugin

public class LombokConventionPlugin : Plugin<Project> {

    public companion object {
        public const val LOMBOK_CONVENTION_PLUGIN_ID: String = "io.github.zenhelix.lombok.convention"

        private const val LOMBOK_PLUGIN_NAME = "io.freefair.lombok"
        private const val KOTLIN_JVM_PLUGIN_NAME = "org.jetbrains.kotlin.jvm"

        private const val LOMBOK_SUB_PLUGIN_CLASS_NAME = "org.jetbrains.kotlin.lombok.gradle.LombokSubplugin"
    }

    override fun apply(target: Project) {
        val extraLombokExtension = target.extensions.create<ExtraLombokExtension>(EXTRA_LOMBOK_EXTENSION_NAME)

        target.afterEvaluate {
            val enabled = extraLombokExtension.enabled.getOrElse(DEFAULT_ENABLED_EXTRA_LOMBOK_PLUGIN)
            if (enabled.not()) {
                return@afterEvaluate
            }

            pluginManager.withPlugin(LOMBOK_PLUGIN_NAME) {
                pluginManager.withPlugin(KOTLIN_JVM_PLUGIN_NAME) {
                    val kotlinLombokInClasspath = runCatching { Class.forName(LOMBOK_SUB_PLUGIN_CLASS_NAME) }.isSuccess
                    val kotlinInClassPath = runCatching { Class.forName("org.jetbrains.kotlin.gradle.plugin.KaptExtension") }.isSuccess
                    if (kotlinLombokInClasspath && kotlinInClassPath) {
                        pluginManager.apply(LombokSubplugin::class)

                        extensions.findByType<KaptExtension>()?.also {
                            extensions.configure<KaptExtension> {
                                keepJavacAnnotationProcessors = true
                            }
                        }
                    }
                }
            }
        }
    }

}