---
name: navigation3-routes
description: Add or change Navigation 3 destinations in this Android/iOS template while keeping navigation in Routes and the app host, with stateless Screens and entry-scoped ViewModels.
---

Read `AGENTS.md`, `app/navigation/AppNavigation.kt`, and the affected feature's presentation/navigation package. Use the catalog's JetBrains multiplatform Navigation 3 UI artifact, not an Android-only replacement.

Use the single app-wide `features.navigation.domain.Navigator` and `features.navigation.domain.model.Destination` contracts. Every Route accepts Navigator directly. Do not create feature-specific navigator interfaces or add methods such as openFeatureDetails to the app-wide interface. Routes call `navigateTo(destination)` or `goBack()`; Screens emit Actions and ViewModels expose state/business effects.

Create feature-owned `@Serializable` destination values implementing Destination in presentation/navigation. Carry small stable arguments such as IDs. Destination is library-independent; it does not extend NavKey. The navigation feature's framework `NavigationEntry` wraps it as a serializable NavKey, and `Navigator3` implements Navigator against the app back stack. Neither shared navigation contracts nor Navigator3 import concrete sibling-feature destinations.

The app host creates one Navigator3 with remember against its saveable back stack and passes that same instance to every Route as Navigator. New destinations require explicit serializer registration under Destination in the app SavedStateConfiguration and a rendering branch in the NavigationEntry entry provider. Preserve NavigationEntry registration under NavKey. Android's reflection-only rememberNavBackStack overload is unsuitable for shared iOS code. Never instantiate another navigator per screen or put navigator implementations into features.

Preserve `rememberSaveableStateHolderNavEntryDecorator()` followed by `rememberViewModelStoreNavEntryDecorator()` on NavDisplay. Resolve Koin ViewModels inside entry content via Routes. Preserve root-pop protection and avoid unintended duplicate destination entries; for multiple instances of a detail screen, include its ID in the key.

For async success/error navigation, model explicit effects from the ViewModel and collect in a lifecycle-aware Route effect. Decide whether an event may be dropped or replayed when inactive; do not add a persistent navigation boolean or navigate from composition. Do not add an effect stream for actions the Route can handle directly.

Verify push/pop, returning state, distinct detail IDs when applicable, lifecycle scoping, and serialization/restoration. Preview Screens with fixtures, not the navigation host.

Official references:
- https://kotlinlang.org/docs/multiplatform/compose-navigation-3.html
- https://developer.android.com/guide/navigation/navigation-3/save-state
