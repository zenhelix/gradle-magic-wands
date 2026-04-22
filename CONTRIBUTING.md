# Contributing to ZenHelix

Thank you for your interest in contributing!

## Getting Started

1. Fork the repository
2. Create a feature branch from `main`
3. Make your changes
4. Run tests: `./gradlew check` (for Gradle projects)
5. Submit a pull request

## Project Types

| Type | Description | Examples |
|---|---|---|
| Kotlin Library | Published to Maven Central | kt-utils, spring-kt, gradle-extensions, dependanger |
| Gradle Plugin | Published to Maven Central + Gradle Portal | gradle-magic-wands, maven-central-publish |
| Application | Deployed, not published to registry | zenhelix-app |

## Creating a New Project

Use one of our template repositories:
- [template-kotlin-library](https://github.com/zenhelix/template-kotlin-library) — single-module Kotlin library
- [template-kotlin-library-multimodule](https://github.com/zenhelix/template-kotlin-library-multimodule) — multi-module Kotlin library
- [template-gradle-plugin](https://github.com/zenhelix/template-gradle-plugin) — single-module Gradle plugin
- [template-gradle-plugin-multimodule](https://github.com/zenhelix/template-gradle-plugin-multimodule) — multi-module Gradle plugin
- [template-go-module](https://github.com/zenhelix/template-go-module) — Go module

## CI/CD

Each project has its own workflow files that delegate to reusable workflows from [ci-workflows](https://github.com/zenhelix/ci-workflows). The naming convention for project workflow files:

| Project Workflow | Trigger | Purpose |
|---|---|---|
| `build-on-branch.yml` | Pull request | Calls check + labeler workflows |
| `build-on-main.yml` | Push to main | Calls check + create-tag workflows |
| `release-on-tag.yml` | Tag push | Calls release + publish workflows |
| `codeql-analysis.yml` | Push to main, PR, weekly | CodeQL security scanning |

## Conventions

- Follow [Conventional Commits](https://www.conventionalcommits.org/) for commit messages
- Format: `<type>: <description>` (types: feat, fix, refactor, docs, test, chore, perf, ci)
- Write tests for new features and bug fixes
- Keep pull requests focused — one feature or fix per PR
- All PRs require at least one approving review

## Reporting Issues

Use the issue templates provided. Include reproduction steps and environment details.
