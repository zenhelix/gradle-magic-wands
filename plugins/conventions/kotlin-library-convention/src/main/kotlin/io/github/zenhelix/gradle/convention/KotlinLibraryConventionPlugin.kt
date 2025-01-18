package io.github.zenhelix.gradle.convention

import io.github.zenhelix.gradle.convention.ExtraKotlinLibraryExtension.Companion.EXTRA_KOTLIN_LIBRARY_EXTENSION_NAME
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension

public class KotlinLibraryConventionPlugin : Plugin<Project> {

    public companion object {
        public const val KOTLIN_LIBRARY_CONVENTION_PLUGIN_NAME: String = "io.github.zenhelix.kotlin-library.convention"
    }

    override fun apply(target: Project) {
        val extraKotlinJvmExtension = target.extensions.create<ExtraKotlinLibraryExtension>(EXTRA_KOTLIN_LIBRARY_EXTENSION_NAME)

        target.afterEvaluate {
            val enabled = extraKotlinJvmExtension.enabled.get()
            if (enabled.not()) {
                return@afterEvaluate
            }

            val kotlinPluginInClasspath = runCatching { Class.forName("org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension") }.isSuccess
            if (kotlinPluginInClasspath) {
                extensions.findByType<KotlinBaseExtension>()?.also {
                    extensions.configure<KotlinBaseExtension> {
                        explicitApi()
                    }
                }
            }
        }
    }
}