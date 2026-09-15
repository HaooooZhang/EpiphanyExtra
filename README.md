# Epiphany Extra

Epiphany Extra provides optional, isolated compatibility integrations for the Epiphany skill-tree system.

## Integrations

- Origins: see [design/origin.md](design/origin.md)
- Oritech: see [design/oritech.md](design/oritech.md)
- Ars Nouveau: see [design/ars.md](design/ars.md)

Each integration is kept in its own top-level source package and degrades safely when its target mod is absent.

Origins: Classes (`origins_classes`) is covered transitively by the Origins integration: use its standard class Layer (`origins_classes:class`), Origin tag (`#origins_classes:class`) or class Power tags (for example `#origins_classes:warrior`) with the existing Origins condition/reward fields. No separate compatibility package is required.

## Origins test data

When Origins is installed, the built-in `epiphany_extra` datapack entries provide a small end-to-end compatibility scenario:

1. Grant/select `origins:avian` in the `origins:origin` layer. The `Origins 精确匹配测试` module should auto-unlock, and the `Origins 标签匹配测试` module should also unlock.
2. Select `Origins 精确 Power` to grant `origins:water_breathing`.
3. Select `Origins Power 标签` to grant the powers in `#origins:avian`; reset or remove the insight to verify source-scoped revocation.
4. Complete `origin_test` to unlock `Origins 兼容完成奖励`, then select it to test the Epiphany reward registry.

The two aptitude entries award separate values for Origin and Power grant events, including a one-time `first_reward` for Avian and Water Breathing. These entries are intentionally bundled as development/test content and should be replaced or overridden by a pack for production progression.

## Oritech test data

When Oritech is installed, the bundled test modules exercise the Condition-only integration:

1. Install `oritech:augment/flight` and check that `oritech_augment_test` unlocks.
2. Toggle the augment off and on; `oritech_augment_enabled_test` should only unlock while it is enabled.

Oritech has no Reward or aptitude integration in the current scope. Without Oritech, these conditions safely remain false.

## Ars Nouveau test data

When Ars Nouveau is installed, the `ars_glyph_test` module checks whether the player has learned `ars_nouveau:glyph_harm` (chosen because it is not in the current player's known-glyph list). Ars mana attributes remain available through Epiphany's generic Attribute Condition and are not duplicated by this integration.
