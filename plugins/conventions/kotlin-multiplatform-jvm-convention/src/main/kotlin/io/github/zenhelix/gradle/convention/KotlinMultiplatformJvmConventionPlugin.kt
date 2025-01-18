package io.github.zenhelix.gradle.convention

import org.gradle.api.Plugin
import org.gradle.api.Project

public class KotlinMultiplatformJvmConventionPlugin : Plugin<Project> {

    public companion object {
        public const val KOTLIN_KMM_JVM_CONVENTION_PLUGIN_NAME: String = "io.github.zenhelix.kmm-jvm.convention"
    }

    override fun apply(target: Project) {

    }

}