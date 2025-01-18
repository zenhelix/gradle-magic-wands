package io.github.zenhelix.gradle.convention

import com.android.build.gradle.BaseExtension
import io.github.zenhelix.gradle.convention.ExtraAndroidCompilationExtension.Companion.DEFAULT_COMPILE_SDK
import io.github.zenhelix.gradle.convention.ExtraAndroidCompilationExtension.Companion.DEFAULT_MIN_SDK
import io.github.zenhelix.gradle.convention.ExtraAndroidCompilationExtension.Companion.DEFAULT_TARGET_SDK
import io.github.zenhelix.gradle.convention.ExtraAndroidCompilationExtension.Companion.EXTRA_ANDROID_EXTENSION_NAME
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create


public class AndroidCompilationConventionPlugin : Plugin<Project> {
    public companion object {
        public const val ANDROID_COMPILATION_CONVENTION_PLUGIN_ID: String = "io.github.zenhelix.android-sdk.convention"

        private const val ANDROID_LIBRARY_PLUGIN_NAME = "com.android.library"
        private const val ANDROID_APPLICATION_PLUGIN_NAME = "com.android.application"
    }

    override fun apply(target: Project) {
        val extraAndroidCompilationExtension = target.extensions.create<ExtraAndroidCompilationExtension>(EXTRA_ANDROID_EXTENSION_NAME)

        val enabled = extraAndroidCompilationExtension.enabled.get()
        if (enabled.not()) {
            return
        }

        val minSdk = extraAndroidCompilationExtension.minSdk.getOrElse(DEFAULT_MIN_SDK)
        val compileSdk = extraAndroidCompilationExtension.compileSdk.getOrElse(DEFAULT_COMPILE_SDK)
        val targetSdk = extraAndroidCompilationExtension.targetSdk.getOrElse(DEFAULT_TARGET_SDK)

        val androidPluginInClasspath = runCatching { Class.forName("com.android.build.gradle.BaseExtension") }.isSuccess
        if (androidPluginInClasspath) {
            target.pluginManager.withPlugin(ANDROID_LIBRARY_PLUGIN_NAME) {
                target.extensions.configure<BaseExtension> {
                    compileSdkVersion(compileSdk)
                    defaultConfig {
                        this.minSdk = minSdk
                        this.targetSdk = targetSdk
                    }
                }
            }
            target.pluginManager.withPlugin(ANDROID_APPLICATION_PLUGIN_NAME) {
                target.extensions.configure<BaseExtension> {
                    compileSdkVersion(compileSdk)
                    defaultConfig {
                        this.minSdk = minSdk
                        this.targetSdk = targetSdk
                    }
                }
            }
        }

    }

}
