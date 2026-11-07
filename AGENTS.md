# Project architecture and contribution rules

This is an Android/iOS Compose Multiplatform template. Keep features as independent packages inside `shared`, ready to move into Gradle modules when needed. These are architectural boundaries even while the compiler sees one module.

## Layout and ownership

Base package: `com.levinzonr.compose.template`.

```text
shared/src/commonMain/kotlin/com/levinzonr/compose/template/
  App.kt                         # Koin lifetime, AppTheme, app navigation
  app/
    di/                          # Aggregate feature modules and app-wide bindings
    navigation/                  # App back stack, host, destination registration
  features/<feature>/
    domain/
      model/                     # Business values, independent of storage/UI
      repository/                # Repository interfaces
      usecase/                   # Business operations
    data/
      repository/                # Repository implementations and mapping
      datasource/                # APIs, Room DAOs/entities, DataStore adapters
    di/                          # Feature Koin module declarations
    presentation/
      navigation/                # Feature destination implementations
      <screen>/
        <Screen>Contract.kt      # Immutable State and Actions
        <Screen>Route.kt         # State collection, action dispatch, navigation
        <Screen>Screen.kt        # One screen composable plus preview composables
        <Screen>ViewModel.kt     # Injected domain collaborators, StateFlow
        components/              # Meaningful screen parts, each with previews
    framework/                   # Optional native adapters/services, not necessarily UI
```

Mirror a feature's package in `androidMain` and `iosMain` for platform implementations. Keep native imports there. Shared `framework` may contain expect declarations and adapters over them. The greeting feature demonstrates all five packages; native platform information belongs to that feature.

## Dependency boundaries

- `domain` owns models, repository interfaces, and use cases. It depends on Kotlin and, when needed, coroutines/Flow. Do not import Compose, lifecycle, Koin, Android/UIKit, Room, DataStore, DTOs, or data implementations there.
- `data` implements domain interfaces, talks to data sources, and translates persistence/network representations into domain values. Room entities and API DTOs must not become UI/domain models by convenience. Implement threading and storage error translation at the appropriate data boundary.
- `presentation` depends on its feature's domain and UI libraries. ViewModels never call DAOs, DataStore, API clients, native services, or repository implementations directly.
- `framework` contains native integration such as notification scheduling, services, permissions, OS storage paths, or platform adapters. Implement inward-facing interfaces so business logic does not depend on the OS. Do not make this a second presentation layer.
- `di` is the composition boundary allowed to import all feature layers. Constructors receive dependencies; do not use Koin service location inside domain/data/ViewModels.
- `app` composes features and implements navigation between them. A feature must not import `app` or another feature's internal data, DI, framework, or presentation implementation. Collaborate through an explicit domain/API contract or app coordination. Extract shared contracts only after a real shared need exists.

Prefer `internal` for implementations, data sources, framework adapters, and ViewModels; expose the minimum route, navigator, destination, domain, and DI entry points the app needs. Kotlin `internal` is module-wide today, so package boundaries also require review. Avoid dependency cycles and global layer buckets such as a root `repositories` package. Do not introduce a Gradle module just to satisfy this layout.

## Screen contract

Every screen has a dedicated package and four files: Contract, Route, Screen, and ViewModel. Its `components` package holds meaningful extracted UI parts.

1. **Contract**: use an immutable `<Screen>State` data class containing UI-ready values. Represent user intent using a sealed `<Screen>Action` interface with data objects/classes, or a `<Screen>Actions` data class of callbacks. Pick one pattern per screen. State contains no ViewModel, navigator, mutable collections, or service references. Defaults must make previews easy.
2. **Route**: accept the app-wide `features.navigation.domain.Navigator` directly as a parameter. Obtain a screen-scoped ViewModel from Koin, collect its StateFlow with `collectAsStateWithLifecycle`, and render the stateless Screen. Handle navigation actions through the navigator and dispatch business actions to ViewModel methods. Never let a Screen mutate the back stack. If asynchronous work produces navigation events, collect them once from the Route in a lifecycle-aware effect; never navigate while rendering or model a one-time event as a sticky boolean.
3. **Screen**: exactly one production composable plus preview composables in `<Screen>Screen.kt`. Accept State, Actions/callbacks, and an optional Modifier. Render values and emit intent. No DI, ViewModel, Flow collection, business state ownership, native calls, navigation, or data access. Extract each meaningful section/card/form/list item to this screen's `components` package instead of adding private production composables to the Screen file. Simple layout primitives and a single Button/Text need no wrapper.
4. **Components**: state and callbacks arrive through parameters. Each meaningful component has its own file and at least one preview with deterministic fixtures and AppTheme. Keep components local until genuine reuse justifies feature-level or shared UI placement.
5. **ViewModel**: constructor-inject feature domain use cases and/or repository interfaces. Expose read-only StateFlow backed by private mutable state. Update immutable state with `copy`/`update`; run suspend work in `viewModelScope`. Handle loading, errors, cancellation, and concurrent requests when the operation warrants them. Do not inject a navigator or hold Compose/native lifecycle objects. Navigation decisions based on business outcomes may be emitted as events; the Route executes navigation.

Stateless means externally owned business/UI state. Compose's internal animation/layout state is fine. A genuinely stateful UI control should expose a stateless rendering API; keep its integration in a Route or dedicated state owner. Previews call Screen/component functions directly with literal data fixtures, AppTheme tokens, and no-op actions, never `App`, Routes, Koin, or a live database. Include meaningful states (for example collapsed/expanded, loading/error/content) that actually exist for the screen.

## Dependency integration

- **Koin**: each feature exports one composite module in `di`, containing only `includes(...)`. Split bindings into separate `<Feature>DataModule.kt` (repositories, data sources, storage), `<Feature>DomainModule.kt` (use cases), and `<Feature>PresentationModule.kt` (ViewModels). Keep layer modules internal and their declarations inside `di`, preserving a Koin-free domain package. The domain DI module must not import data implementations. Add a framework/platform module when native bindings warrant one. Layer modules do not include one another or duplicate bindings. Register only the composite in `app/di/AppModules.kt`, never each included module separately. Use the configuration overload: `KoinApplication(configuration = koinConfiguration { modules(appModules) })`; the old `application = { ... }` overload is deprecated in Koin 4.2. Keep the configuration stable outside recomposition. The single `KoinApplication` in `App` manages the app container. Use `single` for repositories and shared storage, `factory` for lightweight use cases, and `viewModelOf`/`viewModel` for ViewModels. Supply native bindings at composition boundaries, not through static activity references. Do not add a second global `startKoin` on Android/iOS.
- **Navigation 3**: there is one Navigator per app, owned by the host for its lifetime. `features/navigation/domain/model/Destination.kt` defines the library-independent destination interface; `features/navigation/domain/Navigator.kt` defines `navigateTo(Destination)` and `goBack()`. Every Route receives that same Navigator interface directly. Never introduce per-feature navigator interfaces or feature-specific methods on the shared Navigator. Features implement Destination with serializable presentation/navigation values. `features/navigation/framework/Navigator3.kt` implements the interface against the app's saveable back stack, wrapping destinations in NavigationEntry (NavKey). The app composes the host and registers both the NavigationEntry serializer under NavKey and each concrete destination serializer under Destination for iOS-safe restoration. Keep saveable-state and ViewModelStore entry decorators, root-pop protection, and duplicate-top protection. Pass small IDs, not services or mutable models. Features may depend on the navigation feature's public domain contracts; they must not depend on its framework implementation. The navigation infrastructure feature currently needs only domain and framework; add data/di/presentation packages when they have real responsibilities rather than placeholder modules.
- **Room**: keep DAOs/entities/database code on the data side; map to domain models in repositories. Build native database paths in framework/native source sets. Register one database per file through DI, with the bundled SQLite driver and appropriate query coroutine context. KSP compiler bindings cover Android, iosArm64, and iosSimulatorArm64. Export and commit schemas in `shared/schemas`; add migrations when schemas change. Do not add a database merely because the template includes Room.
- **DataStore**: wrap settings persistence in a feature data source/repository behind a domain interface. Create one store per file, bind it as a singleton, and provide platform-specific storage paths from framework. Use transactional edits, immutable mapped domain values, and Flow for observation; never construct a store during recomposition. Avoid swallowing coroutine cancellation or silently replacing all read failures with defaults. Do not persist demo state solely to exercise the dependency.
- **Spotless/Detekt**: formatting belongs to Spotless/ktlint; architecture and Kotlin analysis to Detekt/review. Follow `.editorconfig` and `config/detekt/detekt.yml`. Scope necessary suppressions narrowly; never disable checks globally to make a feature pass. Do not claim Detekt enforces package boundaries unless such rules are actually added.

Versions and aliases belong in `gradle/libs.versions.toml`. Verify APIs against the installed version and official documentation when changing integrations; documentation may describe Room 3 while this template uses Room 2.

## Validation

Read `skills/agent-testing/SKILL.md` for every behavior or UI change. Follow [docs/testing.md](docs/testing.md) and verify your own changes before handing them back.

- Business logic uses TDD: write a failing behavior test, run it and confirm the expected failure, implement the minimum change, then refactor with tests green. Use explicit GIVEN / WHEN / THEN sections and behavior-oriented test names.
- Use small configurable fakes behind domain/data-source contracts, placed in mirrored test packages. Construct subjects directly; avoid live Koin, network, storage, native services, mocks, and real-time sleeps in business tests. Test observable outcomes rather than private calls.
- Use Maestro flows under `.maestro/` for user interactions and navigation. Add or update the relevant flow with each changed journey. Assert outcomes after actions, use accessible text or stable IDs, and keep flows independent.
- For stateless screens/components, add deterministic previews of meaningful states and render/inspect them for simple visual checks. Use Android Studio's preview renderer when available. Check layout, text, clipping, and light/dark contrast. A compiled preview alone is not visual verification; previews do not prove interaction behavior.
- Report exact commands, results, rendered previews, and blocked checks. Never describe an unrun test or uninspected preview as passing.

Add meaningful tests for domain behavior, data mapping/error handling, and ViewModel state transitions with fake domain collaborators. Mirror feature packages under `commonTest`; use platform tests for actual native integration. Do not retain arithmetic placeholder tests or tests that only check file names.

```shell
./gradlew spotlessApply
./gradlew spotlessCheck detekt
./gradlew :shared:testAndroidHostTest :androidApp:assembleDebug
# On macOS with the required simulator:
./gradlew :shared:iosSimulatorArm64Test
```

Root `check` includes formatting and Detekt. State exactly which checks ran and which were blocked; an unavailable Gradle distribution/network or iOS toolchain is not a successful build.

## Project skills

The workspace's `.agents` directory is read-only. Repository skills therefore live in `skills/`; read the applicable `SKILL.md` explicitly before using it. These files travel with the template; they are not automatically installed globally. If the environment later supports a writable `.agents/skills`, they may be installed there without changing their content.

- `skills/feature-architecture/SKILL.md`: create/refactor a feature and its stateless screen pipeline.
- `skills/koin-feature-di/SKILL.md`: bind feature collaborators and integrate the app container.
- `skills/navigation3-routes/SKILL.md`: add destinations, route-owned navigation, and entry-scoped state.
- `skills/feature-persistence/SKILL.md`: integrate Room or DataStore behind feature domain interfaces.
- `skills/kotlin-quality/SKILL.md`: format/analyze the project and validate architecture changes.

## Standalone design system

`:designsystem` is a Kotlin Multiplatform library for Android, iosArm64, and iosSimulatorArm64. It owns all reusable visual foundations and UI primitives under `com.levinzonr.compose.template.designsystem`:

```text
designsystem/src/commonMain/kotlin/com/levinzonr/compose/template/designsystem/
  theme/
    AppTheme.kt        # Theme provider and object API
    AppColors.kt       # Semantic light/dark colors and Material mapping
    AppDimensions.kt   # Spacing, padding, component sizing tokens
    AppTypography.kt   # Shared text roles, initially Material 3 typography
    AppShapes.kt       # Shared shape roles, initially Material 3 shapes
  components/
    AppButton.kt       # Shared action button with state previews
    AppText.kt         # Shared text with typography/color defaults
```

The dependency direction is `shared -> designsystem -> Compose`. The design system must never depend on `shared`, features, domain models/use cases, navigation, Koin, persistence, or native business services. Other Compose modules can consume it directly using `implementation(project(":designsystem"))`. It is a real Gradle module, unlike the feature packages. Compose types present in its public API are exposed with `api` dependencies.

### Theme and token rules

Wrap app UI and every screen/component preview in the custom `AppTheme(...)` provider. Its companion object-style API is the only token entry point for feature UI:

```kotlin
AppTheme.colors.primary
AppTheme.colors.onPrimary
AppTheme.dimensions.contentGap
AppTheme.typography.titleMedium
AppTheme.shapes.medium
```

- Feature/app UI must use these tokens for colors, typography, dimensions, and shapes. Do not read MaterialTheme directly, create another feature theme, or instantiate Color, TextStyle, Typography, Shapes, or rounded shapes with local style constants. Do not hardcode hex/RGB colors, named Color constants, font sizes/weights/line heights, dp/sp sizes, corner radii, spacing, padding, or custom alpha/elevation values in feature UI, including previews. Layout proportions and runtime-measured values are not fixed style tokens; system insets remain system-derived.
- If a needed color or token is missing, add it to the design system **first**, give it a meaningful role name, then consume it from the feature. Do not introduce a local constant or inline literal as a temporary substitute. Add both light/dark semantic color values and update the Material mapping when the new role corresponds to a Material role. Custom roles need not be forced into an unrelated Material slot.
- `AppColors` currently wraps the used semantic roles; extend it for additional roles. Typography and Shapes currently use Material 3's role types/defaults through AppTheme. Raw design values and Material-specific mapping belong exclusively in the design system, preferably in theme/token definitions. Feature code must not use `.copy(fontSize = ...)` or color `.copy(alpha = ...)` to invent a missing variant.
- Prefer paired foreground/background roles (`primary`/`onPrimary`, `primaryContainer`/`onPrimaryContainer`, `surface`/`onSurface`). Keep contrast valid in light and dark themes. Material defaults provide the initial complete underlying palette; the custom colors are mapped into it. Extend the custom API before exposing additional Material roles to consumers.
- Use `AppButton` and `AppText` for their existing roles. Add new shared variants in the module instead of styling a Material Button/Text differently in a feature. It is fine to use Compose layout/animation primitives in feature rendering; they consume AppTheme tokens for visual values. Additional Material primitives may be used with AppTheme tokens where no shared component exists, after evaluating whether they belong in the design system.
- The provider accepts token overrides for alternate brands, previews, and tests. Production overrides must still be defined centrally in the design system; feature code must not construct ad hoc token sets. Light/dark mode follows the system by default, while previews should set `darkTheme` explicitly for deterministic rendering.

### Decide component ownership before implementation

Whenever adding or changing a feature UI component, first inspect `designsystem/components` for an existing component/variant and ask whether this new part is a reusable UI primitive or pattern.

- Put generic buttons, inputs, badges, reusable cards, loading/error presentation patterns, and similarly domain-independent visuals into `:designsystem` when their API can be expressed with display values, slots, callbacks, and Modifier. A clear generic primitive can belong there on its first use; do not require two copied implementations before sharing it.
- Keep business-specific compositions in the feature screen's `components` package. GreetingContent knows about the greeting illustration/message, and PlatformInfo labels native platform information; they remain feature-owned while using shared AppText and theme tokens.
- Do not move a feature-specific widget into the design system merely because it contains several composables. Extract the generic visual primitive and keep domain mapping/composition in the feature. Never leak a feature State, Action, model, ViewModel, navigator, resource dependency, or repository into a shared component API.
- Shared components remain stateless, accessible, and previewable without DI or the app. Accept external values/callbacks and Modifier; preserve Material semantics, disabled behavior, and touch targets when wrapping it. Add previews for meaningful states and light/dark themes. Screen-specific components also retain their previews and the stateless rules above.

Spotless and Detekt include `designsystem` sources and its Gradle file. Validate consuming Android builds and iOS compilation when the relevant toolchain is available. Use `skills/compose-design-system/SKILL.md` whenever adding UI tokens, styling, reusable components, or deciding between shared and feature ownership.
