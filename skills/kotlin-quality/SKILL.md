---
name: kotlin-quality
description: Run and maintain Spotless, Detekt, and focused architecture validation for Kotlin feature changes in this Compose Multiplatform template.
---

Read `AGENTS.md`, the root Gradle file, `.editorconfig`, and `config/detekt/detekt.yml` before changing quality configuration.

Follow `skills/agent-testing/SKILL.md` for behavioral verification: TDD with GIVEN / WHEN / THEN and fakes, Maestro flows for interactions, and rendered previews for stateless UI checks. Gradle quality checks alone do not verify these journeys or visuals.

Run the checks appropriate to the change:

```shell
./gradlew spotlessApply
./gradlew spotlessCheck detekt
./gradlew :shared:testAndroidHostTest :androidApp:assembleDebug
```

Run iOS simulator tests on macOS when native changes need them. Root `check` includes Spotless and Detekt. Spotless owns Kotlin/Gradle formatting; keep formatting out of Detekt to avoid conflicting formatters. Detekt is configured at the root across all platform source sets to avoid coupling to Android's Gradle DSL.

Review feature boundaries separately: domain must not import infrastructure/UI; presentation must not import data/framework implementations; features must not import app or sibling internals. Check stateless Screens, deterministic component previews, lifecycle-aware Route collection, injected ViewModels, and module registration. Include `designsystem` in checks; review feature styling for raw colors/dp/sp/radii and direct MaterialTheme reads, and ensure generic primitives use the shared module. Standard Detekt does not enforce these package rules automatically.

Fix findings at their source. Preserve Compose naming allowances; scope any platform entry-point naming suppression to that declaration. Do not add blanket baselines, disable all style rules, or suppress real business issues simply to pass the template.

Add tests for changed behavior (state transitions, repository mapping, storage semantics, domain rules), not tests that duplicate source text. Use fake domain collaborators for ViewModel tests. Add coroutine test dependencies through the catalog only when async behavior requires them.

If tooling cannot execute because of network/sandbox/toolchain limits, report the exact blocked checks. Parsing a version catalog or running a formatter is useful but does not prove a successful Gradle build or native compilation. Do not silently change dependency versions, JVM configuration, or permission settings to evade the environment restriction.
