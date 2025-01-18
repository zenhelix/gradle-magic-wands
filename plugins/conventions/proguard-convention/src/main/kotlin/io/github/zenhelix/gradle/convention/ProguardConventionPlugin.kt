package io.github.zenhelix.gradle.convention

import io.github.zenhelix.gradle.convention.ExtraProguardExtension.Companion.DEFAULT_PROGUARD_VERSION
import io.github.zenhelix.gradle.convention.ExtraProguardExtension.Companion.EXTRA_PROGUARD_EXTENSION_NAME
import io.github.zenhelix.gradle.convention.tasks.CreateComposeDesktopProguardFileTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.desktop.DesktopExtension
import org.jetbrains.compose.desktop.application.tasks.AbstractProguardTask

public class ProguardConventionPlugin : Plugin<Project> {

    public companion object {
        public const val PROGUARD_CONVENTION_PLUGIN_NAME: String = "io.github.zenhelix.proguard.convention"

        public const val PROGUARD_TASK_GROUP_NAME: String = "proguard"

        private const val KOTLIN_COMPOSE_PLUGIN_NAME = "org.jetbrains.compose"

        private const val COMPOSE_PLUGIN_CLASS_NAME = "org.jetbrains.compose.ComposeExtension"
    }

    override fun apply(target: Project) {
        val proguardExtension = target.extensions.create<ExtraProguardExtension>(EXTRA_PROGUARD_EXTENSION_NAME)

        val composeInClassPath = runCatching { Class.forName(COMPOSE_PLUGIN_CLASS_NAME) }.isSuccess
        if (composeInClassPath.not()) {
            return
        }

        target.pluginManager.withPlugin(KOTLIN_COMPOSE_PLUGIN_NAME) {
            val composeDesktopProguardFileTask = target.tasks.register<CreateComposeDesktopProguardFileTask>("proguardComposeDesktopFile") {
                group = PROGUARD_TASK_GROUP_NAME
            }

            target.tasks.withType<AbstractProguardTask>().configureEach {
                dependsOn(composeDesktopProguardFileTask)
            }

            target.extensions.configure<ComposeExtension> {
                this.extensions.configure<DesktopExtension> {
                    application {
                        buildTypes {
                            release {
                                proguard {
                                    version.set(DEFAULT_PROGUARD_VERSION)
                                    configurationFiles.from(composeDesktopProguardFileTask.get().outputFile)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
