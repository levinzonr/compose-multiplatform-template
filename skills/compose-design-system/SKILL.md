---
name: compose-design-system
description: Add or consume AppTheme tokens and reusable Compose components in this template's standalone design system, including deciding whether new feature UI belongs in the shared module.
---

Read the root `AGENTS.md` design-system and screen rules. Inspect `designsystem/src/commonMain/kotlin/com/levinzonr/compose/template/designsystem/theme` and `components` before implementing UI. The module is consumed via `implementation(project(":designsystem"))` and must remain independent of app/features, DI, navigation, domain, and storage.

## Choose ownership first

For every new screen component, consider whether it represents a reusable visual primitive/pattern rather than a business-specific composition. Reuse an existing design-system component or add an appropriate variant before creating a parallel feature version. A clearly generic component can be shared on first use.

Generic buttons, fields, badges, and domain-independent patterns belong in `designsystem/components`; expose display values, slots/callbacks, and Modifier. Feature State/Action/domain types never enter its API. Keep business-specific assembly in the screen's components package, using those primitives. Do not promote an entire feature widget solely to avoid local composition. GreetingContent and PlatformInfo are examples of feature-owned content; AppButton and AppText are shared primitives.

## Consume or extend tokens

Use only `AppTheme.colors`, `AppTheme.dimensions`, `AppTheme.typography`, and `AppTheme.shapes` in feature/app rendering. Use AppButton/AppText for existing shared roles. Compose layout/animation primitives remain available; fixed styling values come from tokens. Never read MaterialTheme directly in consumers or hardcode colors, font size/weight, spacing, dp/sp dimensions, corner radii, alpha, or elevation, including in previews. System insets, layout proportions, and runtime measurements are not fixed styling constants.

If a role is missing, implement it in the module first:

1. Check for an existing equivalent role; avoid aliases named after a specific screen or a raw number.
2. Add a semantic token in the appropriate theme file. For colors, extend AppColors and supply light/dark values. Update `toMaterialColors` when mapping a Material role; keep genuinely custom roles independent. All literal design values stay inside this module.
3. Keep underlying Material defaults and exposed custom roles consistent. Typography/Shapes currently reuse Material 3 types, accessed exclusively through AppTheme. Define style/shape customizations in their token files, not through consumer-side `.copy(...)` calls.
4. Use paired foreground/background roles and verify both themes. Prefer purpose names such as contentGap over feature-specific names. Component defaults refer to theme roles rather than duplicating design constants.
5. Update the consumer only after the token/component exists.

## Components and previews

Keep reusable components stateless and accessible; preserve click/disabled semantics and minimum touch targets when wrapping Material. Avoid unlimited color/font/radius parameters that make each feature invent its own variant. Named shared variants should express intentional design choices. Accept Modifier and external callbacks; do not resolve services or start effects that own business state.

Wrap the app and each preview in `AppTheme`. Set `darkTheme = false` or `true` explicitly in previews. Add previews covering real component states (for example enabled/disabled) and light/dark themes. Render screen previews directly with fixture contracts, without Koin/Routes. Feature-specific previews obey the same token rules.

Run Spotless/Detekt, validate this skill after edits, and build the consuming module when tooling permits. Review feature imports and style literals; stock Detekt does not automatically enforce token ownership. Report blocked checks honestly. Do not add a new design-system dependency on a feature to make a component compile.
