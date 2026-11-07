---
name: feature-persistence
description: Implement Room or DataStore persistence behind feature domain interfaces in this Compose Multiplatform template, with native storage setup and Koin bindings.
---

Read `AGENTS.md`, `shared/build.gradle.kts`, the version catalog, and the affected feature. Choose Room for relational records/queries and DataStore Preferences for settings. Add storage only when requested behavior needs it.

Domain owns repository interfaces and domain models. Data owns database entities/DAOs, settings keys/adapters, and repository implementations. Framework owns native paths/storage construction. DI binds storage and repositories; ViewModels use domain interfaces/use cases and never access persistence APIs. Map persistence models into domain values before exposing them.

## Room

This template uses `androidx.room` 2.x. Official docs may now show `androidx.room3`; do not mix package/artifact generations without an intentional migration.

1. Put entities and DAOs under feature data/datasource (a local subpackage is appropriate). Define the RoomDatabase and `@ConstructedBy`/expected constructor using APIs supported by the catalog version. Let KSP generate constructor actuals; do not handwrite conflicting actual implementations.
2. Keep common database configuration shared, and supply Android application-context/iOS directory paths from native framework code. Use the bundled SQLite driver and an IO query coroutine context. Keep platform imports out of common domain/presentation.
3. Retain Room's compiler dependency for each target: kspAndroid, kspIosArm64, kspIosSimulatorArm64. Use the configured schemaDirectory, commit generated schemas, and define/test migrations rather than enabling destructive migration by default.
4. Register a single database per file in Koin, derive DAOs from it, and bind repository implementations to domain interfaces. Scope transactions around operations that must succeed atomically.
5. Test meaningful mapping/query/migration behavior; use native/instrumented database tests where required. Unit tests with fake DAOs are useful but do not prove SQL or migration correctness.

## DataStore Preferences

1. Keep keys and reads/transactional edits in a feature data source; expose domain-friendly Flow values and suspend mutations through the domain repository.
2. Construct exactly one store per file using the installed DataStore version's supported factory/storage APIs. Provide Android/iOS paths from framework and own the application-lifetime coroutine scope/container lifecycle. Do not create storage from a composable or a per-screen ViewModel.
3. Bind the store/data source as singletons in Koin. Centralize file ownership so another feature cannot independently open the same file. Use feature-scoped file names unless settings are deliberately app-wide.
4. Apply defaults to absent keys. Handle expected IO failures deliberately, propagate cancellation, and avoid catch-all exception handling that hides corruption/programming failures. Avoid persisting secrets in plain preferences without a separately specified secure-storage design.
5. Test defaults, updates, mapped observations, and relevant failures using temporary storage and deterministic coroutine scheduling when needed.

Official references (check against the installed versions):
- https://developer.android.com/kotlin/multiplatform/room
- https://developer.android.com/kotlin/multiplatform/datastore
