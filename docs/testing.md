# Agent verification

The template uses kotlin.test in shared/commonTest, Koin in production, and constructor-injected test fakes. Existing tests cover greeting mapping/use cases/ViewModels and navigation. No mocking, Robolectric, coverage, or screenshot baseline runner is configured. Maestro covers app interactions; Android Studio renders existing Compose previews for visual smoke checks.

## Business logic: TDD and fakes

For a new rule or bug fix, write a test first, run it to confirm the expected behavioral failure, implement the change, and rerun until green. Refactor with tests green. A compilation or environment failure does not count as the red step.

Use explicit `// GIVEN`, `// WHEN`, `// THEN` sections. Name tests after the starting condition, action, and expected result. Keep one behavior per test. Reuse configurable fakes in feature-local commonTest packages; see FakeGreetingRepository and GreetingViewModelTest. Construct subjects directly rather than starting Koin.

Test domain outcomes, data mapping/error handling, and immutable ViewModel state transitions. Add failure/cancellation/concurrency tests only where those behaviors exist. For asynchronous logic use coroutines-test virtual time and controlled dispatchers; never real-time sleeps. Keep actual native/storage integration in platform tests.

Run a focused JVM test while iterating, then the suite:

```shell
./gradlew :shared:testAndroidHostTest --tests '*GreetingViewModelTest'
./gradlew spotlessApply
./gradlew spotlessCheck detekt
./gradlew :shared:testAndroidHostTest :androidApp:assembleDebug
```

On macOS with the required simulator, also run `./gradlew :shared:iosSimulatorArm64Test`.

## UI interactions: Maestro

Flows live in `.maestro/`. Install the Maestro CLI following [official instructions](https://docs.maestro.dev/getting-started/installing-maestro), start a dedicated Android emulator/device, then:

```shell
./gradlew :androidApp:installDebug
maestro test .maestro/
```

The greeting flow clears app data, checks expand/collapse, opens details, returns through the app Back button, verifies entry state preservation, and returns through system back. It targets the Android application ID; iOS needs its own installed app ID and platform-appropriate flows.

Each flow must be independently runnable. Start from known fixtures/state, use accessible text or stable IDs, and assert outcomes after actions. Avoid coordinates, fixed sleeps, OS-version-specific text, and live backend dependencies. Add flows when implementing new journeys; use test-only DI configuration if controlled backend failures are required. Do not introduce fake behavior into production routes.

Maestro assertions retry automatically: see [assertVisible](https://docs.maestro.dev/reference/commands-available/assertvisible). Reset behavior follows [launchApp](https://docs.maestro.dev/reference/commands-available/launchapp). Clear-state flows delete data: use a dedicated test device.

## Stateless UI: preview checks

Create previews alongside Screens/components with literal fixtures, no-op callbacks, AppTheme, and explicit light/dark mode. Cover actual states such as collapsed/expanded, loading, error, or disabled only when supported. No Routes, Koin, ViewModels, or databases.

After building, render previews with Android Studio's `studio_render_compose_preview` tool (filePath and composableName). Inspect both the image and semantics for expected text/visibility, clipping, overlap, and contrast. Start with GreetingScreenPreview, GreetingScreenExpandedPreview, and GreetingDetailsScreenPreview. Render affected component previews as well. For a visual change, capture the existing preview before editing when the renderer is available.

These are visual smoke checks, with no committed pixel baselines. They do not test taps, callbacks, or navigation; Maestro covers those. Report preview names inspected and any render failures rather than claiming preview compilation proves correct visuals.

## Completion evidence

Report the focused test red/green results for new business behavior, suite/build/quality results, Maestro flow results and device, and rendered preview names. Clearly distinguish passed, failed, and blocked checks. Missing Maestro, device, Gradle dependencies, preview renderer, or iOS toolchain means that check remains unverified.
