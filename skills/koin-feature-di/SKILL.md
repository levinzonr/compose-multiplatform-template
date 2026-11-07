---
name: koin-feature-di
description: Wire Koin dependencies for a feature in this template, including domain bindings, platform adapters, screen ViewModels, and app container composition.
---

Read `AGENTS.md` and the current feature module plus `app/di/AppModules.kt` and `App.kt`.

Keep Koin declarations in feature `di`. Bind repository interfaces to data implementations; inject dependencies by constructor throughout domain, data, framework, and ViewModels. Do not call `getKoin`, `inject`, or `koinInject` inside business/data classes. Resolve screen ViewModels only in Routes using `org.koin.compose.viewmodel.koinViewModel`.

Expose one composite feature module. Keep each layer's bindings in its own file within feature `di`; the composite contains `includes(...)` only:

```kotlin
// FeatureDataModule.kt
internal val featureDataModule = module {
    single<FeatureRepository> { DefaultFeatureRepository(get()) }
}

// FeatureDomainModule.kt
internal val featureDomainModule = module {
    factory { ObserveFeatureUseCase(get()) }
}

// FeaturePresentationModule.kt
internal val featurePresentationModule = module {
    viewModelOf(::FeatureViewModel)
}

// FeatureModule.kt — the feature's exported DI entry point
val featureModule = module {
    includes(featureDataModule, featureDomainModule, featurePresentationModule)
}
```

Data modules own repository implementations, data sources, and storage bindings. Domain modules declare use cases and depend on domain contracts; they do not import data implementations. Presentation modules declare ViewModels. Keep Koin declarations in `di`, so the domain package itself remains Koin-free. Add a separate framework/platform module when native bindings grow; include it from the composite rather than accumulating unrelated definitions there. Layer modules do not include each other or register duplicate bindings. Their dependencies are resolved when the composite is loaded.

Use `single` for shared databases, DataStore files, and repositories; `factory` for lightweight use cases; ViewModel definitions for lifecycle-managed presentation state. Where native parameters are needed, register application-context/path factories through platform composition; never retain an Activity in a singleton. The greeting framework adapter demonstrates a shared adapter around expect/actual platform code.

Add only the exported composite feature module to `appModules`; do not register its layer modules separately. `App` uses one Compose `KoinApplication(configuration = appConfiguration)` for Android/iOS, where a stable `appConfiguration` is built with `org.koin.dsl.koinConfiguration { modules(appModules) }`. Use this overload; `KoinApplication(application = { ... })` is deprecated in Koin 4.2; do not add parallel `startKoin` calls. Run Routes inside Navigation 3 entry decorators to obtain an entry-specific ViewModelStoreOwner. Previews render stateless Screens/components and never need a container.

For tests, prefer direct constructor injection of fakes. When validating DI itself, use an isolated `koinApplication` and close it; provide native fakes and verify relevant resolution paths without changing the global container. Inspect installed Koin APIs before using graph-verification helpers, especially for ViewModels or platform parameters.

Official API reference: https://insert-koin.io/docs/reference/koin-compose/compose/ . Use the project's version catalog; a compiler/annotation plugin is not required for this DSL setup.
