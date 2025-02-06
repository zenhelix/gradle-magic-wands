package io.github.zenhelix.gradle.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.tasks.JvmConstants.COMPILE_JAVA_TASK_NAME
import org.gradle.api.internal.tasks.JvmConstants.PROCESS_RESOURCES_TASK_NAME
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.allopen.gradle.AllOpenGradleSubplugin
import org.jetbrains.kotlin.allopen.gradle.SpringGradleSubplugin
import org.jetbrains.kotlin.gradle.internal.Kapt3GradleSubplugin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.noarg.gradle.KotlinJpaSubplugin
import org.jetbrains.kotlin.noarg.gradle.NoArgGradleSubplugin

/**
 * @see [-Xjsr305=strict](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.kotlin.null-safety)
 */
public class SpringConventionPlugin : Plugin<Project> {

    public companion object {
        public const val SPRING_CONVENTION_PLUGIN_ID: String = "io.github.zenhelix.spring.convention"

        private const val KOTLIN_JVM_PLUGIN_NAME = "org.jetbrains.kotlin.jvm"
    }

    override fun apply(target: Project) {
        target.pluginManager.withPlugin(KOTLIN_JVM_PLUGIN_NAME) {
            with(target.pluginManager) {
                val kaptPluginInClasspath = runCatching { Class.forName("org.jetbrains.kotlin.gradle.internal.Kapt3GradleSubplugin") }.isSuccess
                if (kaptPluginInClasspath) {
                    apply(Kapt3GradleSubplugin::class)
                }

                val allOpenPluginInClasspath = runCatching { Class.forName("org.jetbrains.kotlin.allopen.gradle.AllOpenGradleSubplugin") }.isSuccess
                if (allOpenPluginInClasspath) {
                    apply(AllOpenGradleSubplugin::class)
                    apply(SpringGradleSubplugin::class)
                }

                val noargPluginInClasspath = runCatching { Class.forName("org.jetbrains.kotlin.noarg.gradle.KotlinJpaSubplugin") }.isSuccess
                if (noargPluginInClasspath) {
                    apply(NoArgGradleSubplugin::class)
                    apply(KotlinJpaSubplugin::class)
                }
            }
        }

        // see https://docs.spring.io/spring-boot/docs/current/reference/html/configuration-metadata.html#appendix.configuration-metadata.annotation-processor.configuring
        with(target) {
            if (tasks.findByName(COMPILE_JAVA_TASK_NAME) != null && tasks.findByName(PROCESS_RESOURCES_TASK_NAME) != null) {
                tasks.named(COMPILE_JAVA_TASK_NAME) {
                    inputs.files(tasks.named(PROCESS_RESOURCES_TASK_NAME))
                }
            }
        }

        target.afterEvaluate {
            pluginManager.withPlugin(KOTLIN_JVM_PLUGIN_NAME) {
                val kotlinPluginInClasspath = runCatching { Class.forName("org.jetbrains.kotlin.gradle.tasks.KotlinCompile") }.isSuccess
                if (kotlinPluginInClasspath) {
                    tasks.withType<KotlinCompile> {
                        compilerOptions {
                            // Especially through the creation, because sometimes freeCompilerArgs can be null
                            freeCompilerArgs.set(
                                freeCompilerArgs.getOrElse(emptyList()).toMutableList().apply {
                                    add("-Xjsr305=strict")
                                }
                            )
                        }
                    }
                }
            }
        }
    }

}