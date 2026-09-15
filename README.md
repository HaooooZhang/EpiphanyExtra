# Epiphany Extra

Epiphany Extra provides optional, isolated compatibility integrations for the Epiphany skill-tree system.

## Integrations

- Origins: see [design/origin.md](design/origin.md)
- Ars Nouveau: see [design/ars.md](design/ars.md)

Each integration is kept in its own top-level source package and degrades safely when its target mod is absent.

## Origins test data

When Origins is installed, the built-in `epiphany_extra` datapack entries provide a small end-to-end compatibility scenario:

1. Grant/select `origins:avian` in the `origins:origin` layer. The `Origins 精确匹配测试` module should auto-unlock, and the `Origins 标签匹配测试` module should also unlock.
2. Select `Origins 精确 Power` to grant `origins:water_breathing`.
3. Select `Origins Power 标签` to grant the powers in `#origins:avian`; reset or remove the insight to verify source-scoped revocation.
4. Complete `origin_test` to unlock `Origins 兼容完成奖励`, then select it to test the Epiphany reward registry.

The two aptitude entries award separate values for Origin and Power grant events, including a one-time `first_reward` for Avian and Water Breathing. These entries are intentionally bundled as development/test content and should be replaced or overridden by a pack for production progression.
