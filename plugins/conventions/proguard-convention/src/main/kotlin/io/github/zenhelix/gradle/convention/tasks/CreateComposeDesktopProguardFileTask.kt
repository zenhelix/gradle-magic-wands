package io.github.zenhelix.gradle.convention.tasks

import io.github.zenhelix.gradle.convention.ExtraProguardExtension.Companion.DEFAULT_OUTPUT_BASE_DIR
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

public abstract class CreateComposeDesktopProguardFileTask : DefaultTask() {

    @get:OutputFile
    public abstract val outputFile: RegularFileProperty

    init {
        outputFile.convention(project.layout.buildDirectory.dir(DEFAULT_OUTPUT_BASE_DIR).map { it.file("compose-desktop.pro") })
    }

    @TaskAction
    public fun createProguardFile() {
        val file = outputFile.asFile.get()

        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.writeText(
                """
# ===> Kotlinx Coroutines Rules
# https://github.com/Kotlin/kotlinx.coroutines/blob/master/kotlinx-coroutines-core/jvm/resources/META-INF/proguard/coroutines.pro
# ServiceLoader support
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Most of volatile fields are updated with AFU and should not be mangled
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# Same story for the standard library's SafeContinuation that also uses AtomicReferenceFieldUpdater
-keepclassmembers class kotlin.coroutines.SafeContinuation {
    volatile <fields>;
}

# These classes are only required by kotlinx.coroutines.debug.internal.AgentPremain, which is only loaded when
# kotlinx-coroutines-core is used as a Java agent, so these are not needed in contexts where ProGuard is used.
-dontwarn java.lang.instrument.ClassFileTransformer
-dontwarn sun.misc.SignalHandler
-dontwarn java.lang.instrument.Instrumentation
-dontwarn sun.misc.Signal

# Only used in `kotlinx.coroutines.internal.ExceptionsConstructor`.
# The case when it is not available is hidden in a `try`-`catch`, as well as a check for Android.
-dontwarn java.lang.ClassValue

# An annotation used for build tooling, won't be directly accessed.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

-keep class kotlinx.coroutines.** { *; }
# <=== Kotlinx Coroutines Rules

# ===> Kotlinx Datetime Rules
-dontwarn kotlinx.datetime.**
# <=== Kotlinx Datetime Rules

# ===> androidx compose Rules
-keep class androidx.compose.runtime.SnapshotStateKt__DerivedStateKt { *; } # VerifyError
# <=== androidx compose Rules
                """.trimIndent()
            )
        }
    }
}