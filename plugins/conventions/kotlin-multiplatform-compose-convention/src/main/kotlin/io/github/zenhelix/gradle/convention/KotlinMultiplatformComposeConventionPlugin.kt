package io.github.zenhelix.gradle.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.extra
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

public class KotlinMultiplatformComposeConventionPlugin : Plugin<Project> {

    public companion object {
        public const val KOTLIN_KMM_COMPOSE_CONVENTION_PLUGIN_NAME: String = "io.github.zenhelix.kmm-compose.convention"
    }

    override fun apply(target: Project) {
        target.extra["org.jetbrains.compose.experimental.uikit.enabled"] = true.toString()
        target.extra["org.jetbrains.compose.experimental.jscanvas.enabled"] = true.toString()
        target.extra["org.jetbrains.compose.experimental.wasm.enabled"] = true.toString()

        target.afterEvaluate {
            val kotlinPluginInClasspath = runCatching { Class.forName("org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension") }.isSuccess
            if (kotlinPluginInClasspath) {
                extensions.configure<KotlinMultiplatformExtension> {

                    sourceSets.commonMain {
                        dependencies {
                            implementation(compose.runtime)

                            implementation(compose.components.uiToolingPreview)
                        }
                    }

                    sourceSets.commonTest {
                        dependencies {
                            implementation(kotlin("test"))
                            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                            implementation(compose.uiTest)
                        }
                    }

                    sourceSets.androidMain {
                        dependencies {
                            //FIXME  implementation? debugImplementation?
                            implementation(compose.preview)
                            implementation("androidx.activity:activity-compose")
                            implementation("androidx.compose.ui:ui-tooling-preview")
                        }
                    }

                    sourceSets.jsMain {
                        dependencies {
                            implementation(compose.html.core)
                        }
                    }

                    val desktopMainSourceName = "jvmMain" //FIXME rename jvm to desktop only for compose if it possible
                    val desktopMainSourceSetExists = sourceSets.findByName(desktopMainSourceName) != null
                    if (desktopMainSourceSetExists) {
                        sourceSets.getByName(desktopMainSourceName) {
                            dependencies {
                                //FIXME  implementation? debugImplementation?
                                implementation(compose.preview)
                                implementation(compose.desktop.currentOs)
                            }
                        }
                    }

                    val desktopTestSourceName = "jvmTest" //FIXME
                    val desktopTestSourceSetExists = sourceSets.findByName(desktopTestSourceName) != null
                    if (desktopTestSourceSetExists) {
                        sourceSets.getByName(desktopTestSourceName) {
                            dependencies {
                                implementation(compose.desktop.currentOs)
                            }
                        }
                    }

                }
            }
        }
    }
}

private val KotlinMultiplatformExtension.compose: org.jetbrains.compose.ComposePlugin.Dependencies
    get() =
        (this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("compose") as org.jetbrains.compose.ComposePlugin.Dependencies