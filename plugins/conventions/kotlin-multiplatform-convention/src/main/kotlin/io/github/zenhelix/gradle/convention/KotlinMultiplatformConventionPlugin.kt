package io.github.zenhelix.gradle.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.extra
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

public class KotlinMultiplatformConventionPlugin : Plugin<Project> {

    public companion object {
        public const val KOTLIN_KMM_CONVENTION_PLUGIN_NAME: String = "io.github.zenhelix.kmm.convention"
    }

    override fun apply(target: Project) {
        target.extra["kotlin.native.ignoreDisabledTargets"] = true.toString()

        target.afterEvaluate {
            val kotlinPluginInClasspath = runCatching { Class.forName("org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension") }.isSuccess
            if (kotlinPluginInClasspath) {
                extensions.configure<KotlinMultiplatformExtension> {
                    withSourcesJar()
                    compilerOptions {
                        // Especially through the creation, because sometimes freeCompilerArgs can be null
                        freeCompilerArgs.set(
                            freeCompilerArgs.getOrElse(emptyList()).toMutableList().apply {
                                add("-Xexpect-actual-classes")
                            }
                        )
                    }
                }
            }
        }
    }

}