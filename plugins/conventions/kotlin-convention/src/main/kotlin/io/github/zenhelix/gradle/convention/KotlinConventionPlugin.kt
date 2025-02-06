package io.github.zenhelix.gradle.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

public class KotlinConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.afterEvaluate {
            val kotlinPluginInClasspath = runCatching { Class.forName("org.jetbrains.kotlin.gradle.tasks.KotlinCompile") }.isSuccess
            if (kotlinPluginInClasspath) {
                tasks.withType<KotlinCompile>().configureEach {
                    compilerOptions {
                        // Especially through the creation, because sometimes freeCompilerArgs can be null
                        freeCompilerArgs.set(
                            freeCompilerArgs.getOrElse(emptyList()).toMutableList().apply {
                                add("-Xcontext-receivers")
                            }
                        )
                    }
                }
            }
        }
    }

}