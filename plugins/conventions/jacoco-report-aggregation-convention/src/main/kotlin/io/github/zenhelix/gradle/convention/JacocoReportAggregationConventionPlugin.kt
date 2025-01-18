package io.github.zenhelix.gradle.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.testing.jacoco.plugins.JacocoReportAggregationPlugin.JACOCO_AGGREGATION_CONFIGURATION_NAME

@Suppress("UnstableApiUsage")
public class JacocoReportAggregationConventionPlugin : Plugin<Project> {
    public companion object {
        public const val JACOCO_REPORT_AGGREGATION_CONVENTION_PLUGIN_ID: String = "io.github.zenhelix.jacoco-report-aggregation.convention"

        private const val JACOCO_REPORT_AGGREGATION_PLUGIN_NAME = "jacoco-report-aggregation"
        private const val JACOCO_PLUGIN_NAME = "jacoco"
    }

    override fun apply(target: Project) {
        target.afterEvaluate {
            pluginManager.withPlugin(JACOCO_REPORT_AGGREGATION_PLUGIN_NAME) {
                dependencies.apply {
                    allprojects
                        .filter { it.pluginManager.hasPlugin(JACOCO_PLUGIN_NAME) }
                        .forEach {
                            add(JACOCO_AGGREGATION_CONFIGURATION_NAME, project(":${it.path}"))
                        }
                }
            }
        }
    }

}