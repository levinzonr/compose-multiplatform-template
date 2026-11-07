---
name: agent-testing
description: Verify business logic with TDD and fakes, UI journeys with Maestro, and stateless rendering with Compose previews.
---

Read AGENTS.md and docs/testing.md. Use this skill for every business behavior, screen, component, or interaction change.

1. Select the smallest verification layer for the change. Keep business rules in commonTest, native integration in platform tests, interactions/navigation in .maestro, and simple visual checks in deterministic previews.
2. For business changes, write a behavior test first using explicit GIVEN / WHEN / THEN sections. Run the focused test and confirm it fails for the missing behavior (not a compile/tooling failure). Implement the minimum fix, rerun, and refactor only while green. For existing behavior, run characterization tests before changing it. Never claim retrospective tests demonstrate a red/green cycle.
3. Reuse or add configurable fakes in feature-local test packages. Inject domain contracts directly into ViewModels and use cases. For suspend/Flow logic, use kotlinx-coroutines-test with virtual time when needed; add its version-catalog alias before using it. Cover applicable loading, success, error, cancellation, and concurrency behavior without inventing unsupported states.
4. Add/update Maestro flows for changed user journeys. Start from a deterministic state, act through accessible controls, assert each important outcome, and check back navigation/state preservation when relevant. Prefer semantic text or stable IDs over coordinates and automatic assertions over sleeps. Use a dedicated test device because clearState deletes app data.
5. Create previews beside each stateless screen/component using literal fixtures, no-op actions, AppTheme, and explicit darkTheme. Cover actual meaningful states. After a successful build, use studio_render_compose_preview with the file path and preview function name; inspect its image and semantics. Check expected text, visibility, clipping/overlap, and contrast. Record which variants were inspected. If unavailable or rendering fails, report that check as blocked; do not substitute compilation for visual verification.
6. Read skills/kotlin-quality/SKILL.md and run appropriate formatting, analysis, tests, and consuming builds. Run Maestro against the installed current build. Report exact results and any environmental blockers.

Do not add a second UI behavior framework or screenshot plugin merely to check a preview. Preview rendering is a visual smoke check, not an automated pixel baseline or interaction test. If repeatable pixel regression testing is requested later, configure a compatible screenshot runner separately and review its baselines.
