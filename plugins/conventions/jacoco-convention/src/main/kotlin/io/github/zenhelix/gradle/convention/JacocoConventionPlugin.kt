package io.github.zenhelix.gradle.convention

import io.github.zenhelix.gradle.convention.ExtraJacocoExtension.Companion.EXTRA_JACOCO_EXTENSION_NAME
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.tasks.JacocoReport

public class JacocoConventionPlugin : Plugin<Project> {

    public companion object {
        public const val JACOCO_CONVENTION_PLUGIN_ID: String = "io.github.zenhelix.jacoco.convention"

        private const val JACOCO_PLUGIN_NAME: String = "jacoco"
    }

    override fun apply(target: Project) {
        val extraJacocoExtension = target.extensions.create<ExtraJacocoExtension>(EXTRA_JACOCO_EXTENSION_NAME)

        target.afterEvaluate {
            val enabled = extraJacocoExtension.enabled.get()
            if (enabled.not()) {
                return@afterEvaluate
            }

            pluginManager.withPlugin(JACOCO_PLUGIN_NAME) {
                tasks.withType<JacocoReport> {
                    reports {
                        xml.required.set(true)
                        html.required.set(true)
                        csv.required.set(false)
                    }
                }
            }
        }
    }

}