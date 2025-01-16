#!/usr/bin/env kotlin

// github-workflows-kt
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:3.1.0")
@file:Repository("https://bindings.krzeminski.it")

// Actions
@file:DependsOn("actions:checkout:v4")
@file:DependsOn("gradle:actions__wrapper-validation:v4")
@file:DependsOn("actions:setup-java:v4")
@file:DependsOn("peter-murray:workflow-application-token-action:v4")
@file:DependsOn("anothrNick:github-tag-action:v1")

import Branches.MAIN_BRANCH_NAME
import Environment.GITHUB_TOKEN_ENV
import Secrets.ZENHELIX_COMMITER_APP_ID
import Secrets.ZENHELIX_COMMITER_APP_PRIVATE_KEY
import io.github.typesafegithub.workflows.actions.actions.Checkout
import io.github.typesafegithub.workflows.actions.actions.Checkout.FetchDepth
import io.github.typesafegithub.workflows.actions.actions.SetupJava
import io.github.typesafegithub.workflows.actions.actions.SetupJava.Distribution.Temurin
import io.github.typesafegithub.workflows.actions.anothrnick.GithubTagAction
import io.github.typesafegithub.workflows.actions.gradle.ActionsWrapperValidation
import io.github.typesafegithub.workflows.actions.petermurray.WorkflowApplicationTokenAction_Untyped
import io.github.typesafegithub.workflows.domain.Mode.Write
import io.github.typesafegithub.workflows.domain.Permission.Contents
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.domain.triggers.PullRequest.Type.Closed
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.expressions.contexts.SecretsContext
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.ConsistencyCheckJobConfig.Disabled

check(KotlinVersion.CURRENT.isAtLeast(2, 1, 0)) { "This script requires Kotlin 2.1.0 or later. Current: ${KotlinVersion.CURRENT}" }

object Secrets {
    val SecretsContext.ZENHELIX_COMMITER_APP_ID by SecretsContext.propertyToExprPath
    val SecretsContext.ZENHELIX_COMMITER_APP_PRIVATE_KEY by SecretsContext.propertyToExprPath
}

object Environment {
    const val GITHUB_TOKEN_ENV = "GITHUB_TOKEN"
}

object Branches {
    const val MAIN_BRANCH_NAME = "main"
}

workflow(
    name = "Create Tag",
    on = listOf(
        Push(branches = listOf(MAIN_BRANCH_NAME)),
        PullRequest(types = listOf(Closed), branches = listOf(MAIN_BRANCH_NAME))
    ),
    permissions = mapOf(Contents to Write),
    sourceFile = __FILE__,
    targetFileName = "build-on-main.yml",
    consistencyCheckJobConfig = Disabled
) {
    job(id = "create_release_tag", name = "Create Release Tag", runsOn = UbuntuLatest) {
        uses(
            name = "Check out",
            action = Checkout(
                fetchDepth = FetchDepth.Value(0),
                ref = expr { github.eventPullRequest.pull_request.merge_commit_sha }
            )
        )
        uses(name = "Set up Java", action = SetupJava(javaVersion = "17", distribution = Temurin))
        uses(name = "Gradle Wrapper Validation", action = ActionsWrapperValidation())
        run(
            name = "Check",
            command = "./gradlew check"
        )
        val token = uses(
            name = "Get Token",
            action = WorkflowApplicationTokenAction_Untyped(
                applicationId_Untyped = expr { secrets.ZENHELIX_COMMITER_APP_ID },
                applicationPrivateKey_Untyped = expr { secrets.ZENHELIX_COMMITER_APP_PRIVATE_KEY }
            )
        ).outputs.token
        uses(
            name = "Bump version and push tag",
            action = GithubTagAction(),
            env = mapOf(
                GITHUB_TOKEN_ENV to expr { token },
                "WITH_V" to false.toString()
            )
        )
    }
}
