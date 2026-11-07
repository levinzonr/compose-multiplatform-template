This is a Kotlin Multiplatform project targeting Android, iOS.

* [/iosApp](./iosApp/iosApp) contains an iOS application. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

* [/shared](./shared/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - [commonMain](./shared/src/commonMain/kotlin) is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    the [iosMain](./shared/src/iosMain/kotlin) folder would be the right place for such calls.
    Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./shared/src/jvmMain/kotlin)
    folder is the appropriate location.

### Running the apps

Use the run configurations provided by the run widget in your IDE's toolbar. You can also use these commands and options:

- Android app: `./gradlew :androidApp:assembleDebug`
- iOS app: open the [/iosApp](./iosApp) directory in Xcode and run it from there.

### Running tests

Use the run button in your IDE's editor gutter, or run tests using Gradle tasks:

- Android tests: `./gradlew :shared:testAndroidHostTest`
- iOS tests: `./gradlew :shared:iosSimulatorArm64Test`

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…

### Included libraries

Dependency versions are managed in `gradle/libs.versions.toml`. The `shared` module includes:

- Koin Core, Compose, and Compose ViewModel integration, plus Koin Test in `commonTest`.
- Navigation 3 for Compose Multiplatform, lifecycle ViewModel integration, and Kotlin serialization for destination keys.
- Room runtime with bundled SQLite. KSP processes Android, iOS device, and iOS simulator sources. Schemas are exported to `shared/schemas` and should be committed when databases are added.
- DataStore Core and Preferences for shared settings storage.

The greeting feature demonstrates Koin startup and feature modules, domain/repository boundaries, native adapters, stateless screens with previews, and Navigation 3 with explicit multiplatform serialization. Room and DataStore are ready for features that need persistence; the demo does not create unused storage.

### Code quality

Spotless formats Kotlin sources and Gradle Kotlin scripts using ktlint and `.editorconfig`.
Detekt analyzes all Android and shared platform sources using `config/detekt/detekt.yml`, with Compose naming conventions supported.
Detekt uses the 2.0 alpha plugin for the modern Gradle toolchain and runs at the root to avoid Android plugin DSL coupling.

```shell
./gradlew spotlessApply
./gradlew spotlessCheck detekt
./gradlew check
```

The root `check` task includes both quality checks. Build and test the Android app with:

```shell
./gradlew :androidApp:assembleDebug :shared:testAndroidHostTest
```


### Feature architecture

Read [AGENTS.md](AGENTS.md) for the full architecture and contribution rules.
The reference feature is [greeting](shared/src/commonMain/kotlin/com/levinzonr/compose/template/features/greeting):

```text
features/greeting/
  data/           # Repository implementation and data-source contracts
  di/             # Composite Koin module including data, domain, presentation modules
  domain/         # Models, repository interfaces, use cases
  framework/      # Native platform adapters (mirrored in Android/iOS sources)
  presentation/   # Navigation API and screen packages
    greeting/     # Contract, Route, Screen, ViewModel, components with previews
    details/      # Same screen pattern, navigated from greeting
```

Features are independent packages that can later move into Gradle modules. The navigation feature supplies the shared Destination/Navigator contracts and Navigator3 adapter. The app owns one navigator instance, DI aggregation, and the navigation host; Routes own state collection and navigation, and Screens/components render explicit state with callbacks. Open Screen/component previews directly without starting Koin.

Project skills live in [skills/](skills/) because this workspace's `.agents` directory is read-only. Read the applicable `SKILL.md` for feature creation, Koin wiring, Navigation 3, persistence, or Kotlin quality checks. They can be copied into `.agents/skills` in a writable checkout for standard skill discovery.


### Design system

The standalone [:designsystem](designsystem) module contains the shared theme and reusable UI components. Other Compose modules can depend on it directly; it has no dependency on the app or features.

```kotlin
AppTheme {
    AppButton(text = "Continue", onClick = onContinue)
    AppText(text = title, style = AppTheme.typography.titleMedium)
}
```

Use `AppTheme.colors`, `.dimensions`, `.typography`, and `.shapes` for all feature styling. The initial colors, typography, and shapes use Material 3, with light/dark support and centralized spacing tokens. Add missing tokens to the design system before using them; feature UI must not hardcode styling values.

Before adding a screen-specific component, consider whether its generic visual API belongs in the shared module. Business-specific compositions stay within the feature and use shared primitives. See [AGENTS.md](AGENTS.md#standalone-design-system) and the [design-system skill](skills/compose-design-system/SKILL.md).
