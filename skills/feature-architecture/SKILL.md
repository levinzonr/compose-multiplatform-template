---
name: feature-architecture
description: Create or refactor a feature in this Compose Multiplatform template using feature-local clean architecture and stateless Contract/Route/Screen/ViewModel presentation.
---

Read `AGENTS.md` at the repository root. Use `features/greeting` as the executable example, preserving feature ownership rather than copying its business behavior.

For a new capability, identify its feature and domain contract first. Keep domain models, repository interfaces, and use cases under that feature. Implement repositories/data sources in data; place platform adapters in framework and mirror packages in Android/iOS source sets. Add each required layer package when implementing a feature, but create framework only for an actual native concern. Avoid speculative APIs, placeholder DAOs, and global layer directories.

Before implementing feature UI, read `skills/compose-design-system/SKILL.md`. Evaluate each proposed component for reuse in `:designsystem`; use AppTheme tokens exclusively and add missing tokens there first.

For each screen:

- Create a dedicated presentation package with Contract, Route, Screen, and ViewModel files.
- Define immutable State and sealed Actions or a callback Actions data class. Convert domain values into UI state in the ViewModel.
- Inject domain collaborators through the ViewModel constructor. Expose read-only StateFlow and test transitions using fake domain dependencies.
- Route accepts the single app-wide Navigator from the navigation feature, resolves the ViewModel, collects state with lifecycle awareness, and dispatches actions. Business actions go to the ViewModel; navigation goes to the navigator. Collect asynchronous effects in a lifecycle-aware effect when required.
- Screen has one production composable plus previews. It receives state/callbacks and emits intent. Extract meaningful sections to its components package, with a preview in every component file. No Koin, Flow, ViewModel, network, storage, or native dependencies inside rendering functions.
- Preview real UI states directly with AppTheme, deterministic values, and no-op callbacks.

Compose the feature DI entry point from separate data, domain, and presentation Koin modules in `di`, each in its own file. Register only that composite in appModules; follow `skills/koin-feature-di/SKILL.md`.

Expose only the entry points needed for app composition and future extraction. Reject sibling-feature implementation imports and app imports from features. Features may depend on the navigation feature’s public Navigator/Destination contracts; concrete destinations remain feature-owned. If extracting later, move the feature's mirrored source/test packages together, expose its API, then enforce the same dependency direction with Gradle.

Use the DI, navigation, persistence, or quality skill in `skills/` only for the integration being changed. Validate behavior rather than merely testing that the folder tree exists.
