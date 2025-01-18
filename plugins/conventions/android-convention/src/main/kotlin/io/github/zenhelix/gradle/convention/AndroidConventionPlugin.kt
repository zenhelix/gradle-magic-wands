package io.github.zenhelix.gradle.convention

import org.gradle.api.Plugin
import org.gradle.api.Project

public class AndroidConventionPlugin : Plugin<Project> {

    public companion object {
        public const val ANDROID_CONVENTION_PLUGIN_ID: String = "io.github.zenhelix.android.convention"
    }

    override fun apply(target: Project) {
    }
}