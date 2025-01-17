package io.github.zenhelix.gradle.convention

import com.android.build.gradle.BaseExtension
import io.github.zenhelix.gradle.convention.JdkExtension.Companion.JDK_EXTENSION_NAME
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

public open class BaseJavaCompilationConventionPlugin(private val targetVersion: JavaVersion) : Plugin<Project> {

    public companion object {
        private const val KOTLIN_JVM_PLUGIN_NAME = "org.jetbrains.kotlin.jvm"
        private const val KOTLIN_MULTIPLATFORM_PLUGIN_NAME = "org.jetbrains.kotlin.multiplatform"
        private const val ANDROID_LIBRARY_PLUGIN_NAME = "com.android.library"
        private const val ANDROID_APPLICATION_PLUGIN_NAME = "com.android.application"
    }

    override fun apply(target: Project) {
        val extraJavaCompilationExtension = target.extensions.create<JdkExtension>(JDK_EXTENSION_NAME)

        target.pluginManager.withPlugin(ANDROID_LIBRARY_PLUGIN_NAME) {
            val androidPluginInClasspath = runCatching { Class.forName("com.android.build.gradle.BaseExtension") }.isSuccess
            if (androidPluginInClasspath) {
                target.extensions.configure<BaseExtension> {
                    compileOptions {
                        sourceCompatibility = targetVersion
                        targetCompatibility = targetVersion
                    }
                }
            }
            target.pluginManager.withPlugin(ANDROID_APPLICATION_PLUGIN_NAME) {
                target.extensions.configure<BaseExtension> {
                    compileOptions {
                        sourceCompatibility = targetVersion
                        targetCompatibility = targetVersion
                    }
                }
            }
        }

        target.afterEvaluate {
            val enabled = extraJavaCompilationExtension.enabled.get()
            if (enabled.not()) {
                return@afterEvaluate
            }

            pluginManager.withPlugin("java") {
                extensions.configure<JavaPluginExtension> {
                    sourceCompatibility = targetVersion
                    targetCompatibility = targetVersion
                }
            }

            pluginManager.withPlugin(KOTLIN_JVM_PLUGIN_NAME) {
                val kotlinInClasspath = runCatching { Class.forName("org.jetbrains.kotlin.gradle.tasks.KotlinCompile") }.isSuccess
                if (kotlinInClasspath) {
                    tasks.withType<KotlinCompile>().configureEach {
                        compilerOptions {
                            jvmTarget.set(JvmTarget.fromTarget(targetVersion.toString()))
                        }
                    }
                }
            }

            pluginManager.withPlugin(KOTLIN_MULTIPLATFORM_PLUGIN_NAME) {
                val kotlinMultiplatformInClasspath = runCatching { Class.forName("org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension") }.isSuccess
                if (kotlinMultiplatformInClasspath) {
                    extensions.configure<KotlinMultiplatformExtension> {
                        targets.withType<KotlinJvmTarget>().configureEach {
                            compilerOptions {
                                jvmTarget.set(JvmTarget.fromTarget(targetVersion.toString()))
                            }
                        }

                        if (pluginManager.hasPlugin(ANDROID_LIBRARY_PLUGIN_NAME) || pluginManager.hasPlugin(ANDROID_APPLICATION_PLUGIN_NAME)) {
                            androidTarget {
                                compilerOptions {
                                    jvmTarget.set(JvmTarget.fromTarget(targetVersion.toString()))
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}