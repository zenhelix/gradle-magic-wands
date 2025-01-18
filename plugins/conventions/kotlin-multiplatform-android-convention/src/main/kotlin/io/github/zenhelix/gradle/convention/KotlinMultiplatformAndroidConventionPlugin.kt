package io.github.zenhelix.gradle.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget

public class KotlinMultiplatformAndroidConventionPlugin : Plugin<Project> {

    public companion object {
        public const val KOTLIN_KMM_COMPOSE_CONVENTION_PLUGIN_NAME: String = "io.github.zenhelix.kmm-android.convention"
    }

    override fun apply(target: Project) {
        target.afterEvaluate {
            val kotlinPluginInClasspath = runCatching { Class.forName("org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension") }.isSuccess
            if (kotlinPluginInClasspath) {
                extensions.configure<KotlinMultiplatformExtension> {
                    targets.withType<KotlinAndroidTarget> {
                    }
                }
            }
        }
    }

}