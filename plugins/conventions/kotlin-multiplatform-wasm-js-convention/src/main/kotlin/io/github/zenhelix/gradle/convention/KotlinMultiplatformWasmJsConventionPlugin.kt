package io.github.zenhelix.gradle.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmJsTargetDsl

public class KotlinMultiplatformWasmJsConventionPlugin : Plugin<Project> {

    public companion object {
        public const val KOTLIN_KMM_WASM_JS_CONVENTION_PLUGIN_ID: String = "io.github.zenhelix.kmm-wasm-js.convention"

        private const val KOTLIN_MULTIPLATFORM_PLUGIN_NAME = "org.jetbrains.kotlin.multiplatform"
    }

    override fun apply(target: Project) {
        target.pluginManager.withPlugin(KOTLIN_MULTIPLATFORM_PLUGIN_NAME) {
            val kotlinPluginInClasspath = runCatching { Class.forName("org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension") }.isSuccess
            if (kotlinPluginInClasspath) {
                val kmmExtension = target.extensions.findByType<KotlinMultiplatformExtension>()
                kmmExtension?.targets?.withType<KotlinWasmJsTargetDsl>()?.all {
                    useEsModules()
                    generateTypeScriptDefinitions()
                    compilerOptions {
                        this.target.set("es2015")
                        // Especially through the creation, because sometimes freeCompilerArgs can be null
                        freeCompilerArgs.set(
                            freeCompilerArgs.getOrElse(emptyList()).toMutableList().apply {
                                add("-Xir-per-file")
                            }
                        )
                    }
                }
            }
        }
    }

}