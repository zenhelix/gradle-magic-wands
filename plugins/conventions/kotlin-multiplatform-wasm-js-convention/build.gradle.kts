gradlePlugin {
    plugins {
        create("kmm-wasm-js.convention") {
            implementationClass = "io.github.zenhelix.gradle.convention.KotlinMultiplatformWasmJsConventionPlugin"
            id = "io.github.zenhelix.kmm-wasm-js.convention"
            displayName = "Gradle plugin for Kotlin Multiplatform WASM JS setup"
            description = "A Gradle plugin that simplifies and enhances the configuration of Kotlin Multiplatform projects specifically for WASM JS"
            tags = listOf("kotlin", "multiplatform", "kmm", "wasm", "js", "build-tools")
        }
    }
}

dependencies {
    compileOnly(pluginsDevPlatform.kotlin.gradle.plugin)
}
