#!/usr/bin/env kotlin

// github-workflows-kt
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:3.1.0")
@file:Repository("https://bindings.krzeminski.it")

// Actions
@file:DependsOn("actions:checkout:v4")
@file:DependsOn("gradle:actions__wrapper-validation:v4")
@file:DependsOn("actions:setup-java:v4")

import io.github.typesafegithub.workflows.actions.actions.Checkout
import io.github.typesafegithub.workflows.actions.actions.SetupJava
import io.github.typesafegithub.workflows.actions.actions.SetupJava.Distribution.Temurin
import io.github.typesafegithub.workflows.actions.gradle.ActionsWrapperValidation
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.ConsistencyCheckJobConfig.Disabled

check(KotlinVersion.CURRENT.isAtLeast(2, 1, 0)) { "This script requires Kotlin 2.1.0 or later. Current: ${KotlinVersion.CURRENT}" }

workflow(
    name = "Build",
    on = listOf(PullRequest()),
    sourceFile = __FILE__,
    targetFileName = "build-on-branch.yml",
    consistencyCheckJobConfig = Disabled
) {
    job(id = "Build", name = "Build", runsOn = UbuntuLatest) {
        uses(name = "Check out", action = Checkout())
        uses(name = "Set up Java", action = SetupJava(javaVersion = "17", distribution = Temurin))
        uses(name = "Gradle Wrapper Validation", action = ActionsWrapperValidation())
        run(
            name = "Check",
            command = "./gradlew check"
        )
    }
}
