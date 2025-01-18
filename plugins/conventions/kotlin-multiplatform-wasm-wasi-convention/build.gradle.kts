gradlePlugin {
    plugins {
        create("kmm-wasm-wasi.convention") {
            implementationClass = "io.github.zenhelix.gradle.convention.KotlinMultiplatformWasmWasiConventionPlugin"
            id = "io.github.zenhelix.kmm-wasm-wasi.convention"
            displayName = "Gradle plugin for Kotlin Multiplatform WASM WASI setup"
            description = "A Gradle plugin that simplifies and enhances the configuration of Kotlin Multiplatform projects specifically for WASM WASI"
            tags = listOf("kotlin", "multiplatform", "kmm", "wasm", "wasi", "build-tools")
        }
    }
}

dependencies {
    compileOnly(pluginsDevPlatform.kotlin.gradle.plugin)
}
