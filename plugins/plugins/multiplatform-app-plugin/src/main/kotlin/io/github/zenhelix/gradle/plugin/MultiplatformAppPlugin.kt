package io.github.zenhelix.gradle.plugin

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import io.github.zenhelix.gradle.convention.AndroidCompilationConventionPlugin
import io.github.zenhelix.gradle.convention.AndroidConventionPlugin
import io.github.zenhelix.gradle.convention.DesktopComposeConventionPlugin
import io.github.zenhelix.gradle.convention.DistributionConventionPlugin
import io.github.zenhelix.gradle.convention.Java21CompilationConventionPlugin
import io.github.zenhelix.gradle.convention.JavaConventionPlugin
import io.github.zenhelix.gradle.convention.KotlinConventionPlugin
import io.github.zenhelix.gradle.convention.KotlinJvmConventionPlugin
import io.github.zenhelix.gradle.convention.KotlinMultiplatformAndroidConventionPlugin
import io.github.zenhelix.gradle.convention.KotlinMultiplatformConventionPlugin
import io.github.zenhelix.gradle.convention.KotlinMultiplatformIosConventionPlugin
import io.github.zenhelix.gradle.convention.KotlinMultiplatformJsConventionPlugin
import io.github.zenhelix.gradle.convention.KotlinMultiplatformJvmConventionPlugin
import io.github.zenhelix.gradle.convention.KotlinMultiplatformWasmJsConventionPlugin
import io.github.zenhelix.gradle.convention.KotlinMultiplatformWasmWasiConventionPlugin
import io.github.zenhelix.gradle.convention.ProguardConventionPlugin
import io.github.zenhelix.gradle.convention.PublishMavenConventionPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

public class MultiplatformAppPlugin : Plugin<Project> {

    public companion object {
        public const val KOTLIN_MULTIPLATFORM_APP_PLUGIN_ID: String = "io.github.zenhelix.kmm-app"
    }

    override fun apply(target: Project) {
        with(target.pluginManager) {

            apply(MavenPublishPlugin::class)
            apply(PublishMavenConventionPlugin::class)

            apply(JavaConventionPlugin::class)

            apply(KotlinConventionPlugin::class)
            apply(KotlinJvmConventionPlugin::class)

            apply(KotlinMultiplatformConventionPlugin::class)
            apply(KotlinMultiplatformJvmConventionPlugin::class)
            apply(KotlinMultiplatformAndroidConventionPlugin::class)
            apply(KotlinMultiplatformIosConventionPlugin::class)
            apply(KotlinMultiplatformJsConventionPlugin::class)
            apply(KotlinMultiplatformWasmJsConventionPlugin::class)
            apply(KotlinMultiplatformWasmWasiConventionPlugin::class)
            apply(KotlinMultiplatformPluginWrapper::class)

            apply(AndroidConventionPlugin::class)
            apply(AppPlugin::class)
            apply(AndroidCompilationConventionPlugin::class)

            apply(ProguardConventionPlugin::class)

            apply(DistributionConventionPlugin::class)

            apply(Java21CompilationConventionPlugin::class)
        }

        //see https://kotlinlang.org/docs/multiplatform-hierarchy.html#see-the-full-hierarchy-template
        target.extensions.configure<KotlinMultiplatformExtension> {
            jvm("desktop")

            androidTarget()

            listOf(
                iosX64(),
                iosArm64(),
                iosSimulatorArm64()
            )

            js {
                binaries.executable()
            }

            //                        @OptIn(ExperimentalWasmDsl::class)
            //                        this.wasmWasi {
            //                            binaries.executable()
            //                        }

            @OptIn(ExperimentalWasmDsl::class)
            wasmJs {
                binaries.executable()
            }

            applyDefaultHierarchyTemplate()
        }

        target.afterEvaluate {
            this.extensions.configure<KotlinMultiplatformExtension> {
                js {
                    browser {
                        commonWebpackConfig {
                            cssSupport {
                                enabled.set(true)
                            }

                            devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                                static = (static ?: mutableListOf()).apply {
                                    // Serve sources to debug inside browser
                                    add(target.rootDir.path)
                                    add(target.projectDir.path)
                                }
                            }
                        }
                    }
                }

                @OptIn(ExperimentalWasmDsl::class)
                wasmJs {
                    browser {
                        commonWebpackConfig {
                            cssSupport {
                                enabled.set(true)
                            }
                            devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                                static = (static ?: mutableListOf()).apply {
                                    // Serve sources to debug inside browser
                                    add(target.rootDir.path)
                                    add(target.projectDir.path)
                                }
                            }
                        }
                    }
                }

            }

            pluginManager.apply(DesktopComposeConventionPlugin::class)
        }

        target.extensions.configure<BaseAppModuleExtension> {
            sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
            sourceSets["main"].res.srcDirs("src/androidMain/res")
            sourceSets["main"].resources.srcDirs("src/commonMain/resources")
        }

    }
}