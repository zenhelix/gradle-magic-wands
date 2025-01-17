package io.github.zenhelix.gradle.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl

public class KotlinMultiplatformJsConventionPlugin : Plugin<Project> {

    public companion object {
        public const val KOTLIN_KMM_JS_CONVENTION_PLUGIN_NAME: String = "io.github.zenhelix.kmm-js.convention"

        private const val KOTLIN_MULTIPLATFORM_PLUGIN_NAME = "org.jetbrains.kotlin.multiplatform"
    }

    override fun apply(target: Project) {
        target.extra["kotlin.incremental.js.ir"] = true.toString()

        target.pluginManager.withPlugin(KOTLIN_MULTIPLATFORM_PLUGIN_NAME) {
            val kotlinPluginInClasspath = runCatching { Class.forName("org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension") }.isSuccess
            if (kotlinPluginInClasspath) {
                val kmmExtension = target.extensions.findByType<KotlinMultiplatformExtension>()
                kmmExtension?.targets?.withType<KotlinJsTargetDsl>()?.all {
                    useEsModules()
                    generateTypeScriptDefinitions()
                    compilerOptions {
                        this.target.set("es2015")
                        freeCompilerArgs.add("-Xir-per-file")
                    }
                }
            }
        }
    }

}