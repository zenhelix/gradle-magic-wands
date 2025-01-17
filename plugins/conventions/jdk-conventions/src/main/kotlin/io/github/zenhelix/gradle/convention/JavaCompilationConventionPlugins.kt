package io.github.zenhelix.gradle.convention

import org.gradle.api.JavaVersion

public class Java8CompilationConventionPlugin : BaseJavaCompilationConventionPlugin(JavaVersion.VERSION_1_8) {
    public companion object {
        public const val JAVA_8_COMPILATION_CONVENTION_PLUGIN_ID: String = "io.github.zenhelix.jdk8.convention"
    }
}

public class Java11CompilationConventionPlugin : BaseJavaCompilationConventionPlugin(JavaVersion.VERSION_11) {
    public companion object {
        public const val JAVA_11_COMPILATION_CONVENTION_PLUGIN_ID: String = "io.github.zenhelix.jdk11.convention"
    }
}

public class Java17CompilationConventionPlugin : BaseJavaCompilationConventionPlugin(JavaVersion.VERSION_17) {
    public companion object {
        public const val JAVA_17_COMPILATION_CONVENTION_PLUGIN_ID: String = "io.github.zenhelix.jdk17.convention"
    }
}

public class Java21CompilationConventionPlugin : BaseJavaCompilationConventionPlugin(JavaVersion.VERSION_21) {
    public companion object {
        public const val JAVA_21_COMPILATION_CONVENTION_PLUGIN_ID: String = "io.github.zenhelix.jdk21.convention"
    }
}