package io.github.zenhelix.gradle.convention.utils

internal val currentArch by lazy {
    val osArch = System.getProperty("os.arch")
    when (osArch) {
        "x86_64", "amd64" -> Arch.X64
        "aarch64"         -> Arch.Arm64
        else              -> error("Unsupported OS arch: $osArch")
    }
}

internal val currentOS: OS by lazy {
    val os = System.getProperty("os.name")
    when {
        os.equals("Mac OS X", ignoreCase = true)  -> OS.MacOS
        os.startsWith("Win", ignoreCase = true)   -> OS.Windows
        os.startsWith("Linux", ignoreCase = true) -> OS.Linux
        else                                      -> error("Unknown OS name: $os")
    }
}

internal enum class Arch(val id: String) {
    X64("x64"),
    Arm64("arm64")
}

internal enum class OS(val id: String) {
    Linux("linux"),
    Windows("windows"),
    MacOS("macos")
}
