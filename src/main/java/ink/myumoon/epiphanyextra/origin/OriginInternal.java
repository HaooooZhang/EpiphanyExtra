package ink.myumoon.epiphanyextra.origin;

import com.iafenvoy.origins.attachment.OriginDataHolder;
import com.iafenvoy.origins.data.layer.LayerRegistries;
import com.iafenvoy.origins.data.origin.Origin;
import com.iafenvoy.origins.data.origin.OriginRegistries;
import com.iafenvoy.origins.data.power.Power;
import com.iafenvoy.origins.data.power.PowerRegistries;
import com.iafenvoy.origins.event.GrantOriginEvent;
import com.iafenvoy.origins.event.GrantPowerEvent;
import ink.myumoon.epiphany.api.AptitudeSourceManager;
import ink.myumoon.epiphany.api.EpiphanyManager;
import ink.myumoon.epiphany.api.ModuleManager;
import ink.myumoon.epiphanyextra.EpiphanyExtra;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayer;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The only class in EpiphanyExtra that directly references Origins classes.
 * It is loaded only after {@code ModList.isLoaded("origins")} succeeds.
 */
final class OriginInternal {
    private static final Set<String> DIAGNOSTICS = ConcurrentHashMap.newKeySet();
    private static final ResourceLocation ORIGIN_GRANTED =
            ResourceLocation.fromNamespaceAndPath(EpiphanyExtra.MODID, "origins_origin_granted");
    private static final ResourceLocation POWER_GRANTED =
            ResourceLocation.fromNamespaceAndPath(EpiphanyExtra.MODID, "origins_power_granted");

    private OriginInternal() {}

    static void registerEvents() {
        NeoForge.EVENT_BUS.addListener(OriginInternal::onGrantOrigin);
        NeoForge.EVENT_BUS.addListener(OriginInternal::onGrantPower);
        EpiphanyExtra.LOGGER.info("Registered Origins grant event bridge");
    }

    static boolean hasOrigin(ServerPlayer player, String reference, Optional<ResourceLocation> layerId) {
        try {
            Registry<Origin> origins = player.server.registryAccess().registryOrThrow(OriginRegistries.ORIGIN_KEY);
            Optional<Holder.Reference<Origin>> exact = parseExactOrigin(origins, reference);
            TagKey<Origin> tag = parseTag(origins, reference);
            if (tag != null && origins.getTag(tag).isEmpty()) {
                warnOnce("origin-tag:" + reference, "Unknown Origins Origin tag " + reference);
                return false;
            }

            OriginDataHolder data = OriginDataHolder.get(player);
            if (layerId.isPresent()) {
                Registry<com.iafenvoy.origins.data.layer.Layer> layers =
                        player.server.registryAccess().registryOrThrow(LayerRegistries.LAYER_KEY);
                Optional<Holder.Reference<com.iafenvoy.origins.data.layer.Layer>> layer = layers.getHolder(layerId.get());
                if (layer.isEmpty()) {
                    warnOnce("layer:" + layerId.get(), "Unknown Origins layer " + layerId.get());
                    return false;
                }
                Holder<Origin> selected = data.getOrigin(layer.get());
                if (selected == null) return false;
                if (exact.isPresent()) return selected.value().equals(exact.get().value());
                if (tag != null) return selected.is(tag);
                return false;
            }

            if (exact.isPresent()) return data.hasOrigin(exact.get());
            if (tag != null) {
                for (Holder<Origin> selected : data.getOrigins().values()) {
                    if (selected.is(tag)) return true;
                }
                return false;
            }
            return false;
        } catch (Exception e) {
            EpiphanyExtra.LOGGER.warn("Failed to evaluate Origins Origin reference '{}'", reference, e);
            return false;
        }
    }

    static void grantPower(ServerPlayer player, String reference, ResourceLocation source) {
        try {
            Registry<Power> powers = player.server.registryAccess().registryOrThrow(PowerRegistries.POWER_KEY);
            Optional<Set<Holder<Power>>> resolved = resolvePowers(powers, reference);
            if (resolved.isEmpty()) return;

            OriginDataHolder data = OriginDataHolder.get(player);
            Set<Holder<Power>> desired = resolved.get();
            Set<Holder<Power>> owned = new HashSet<>(data.getEntityPowers().get(source));

            // Reconcile the source rather than blindly adding. This removes
            // powers that stopped matching a tag after a datapack reload while
            // keeping apply idempotent and avoiding duplicate grant events.
            for (Holder<Power> holder : owned) {
                if (!desired.contains(holder)) data.revokePower(source, holder);
            }
            for (Holder<Power> holder : desired) {
                if (!owned.contains(holder)) data.grantPower(source, holder);
            }
        } catch (Exception e) {
            EpiphanyExtra.LOGGER.warn("Failed to grant Origins Power reference '{}'", reference, e);
        }
    }

    static void revokePower(ServerPlayer player, String reference, ResourceLocation source) {
        try {
            // Source ownership is authoritative: this also removes powers that
            // stopped matching a tag after a datapack reload.
            OriginDataHolder.get(player).revokeAllPowers(source);
        } catch (Exception e) {
            EpiphanyExtra.LOGGER.warn("Failed to revoke Origins Power reference '{}'", reference, e);
        }
    }

    private static Optional<Holder.Reference<Origin>> parseExactOrigin(Registry<Origin> registry, String reference) {
        if (reference.startsWith("#")) return Optional.empty();
        ResourceLocation id = ResourceLocation.tryParse(reference);
        if (id == null) {
            warnOnce("origin:" + reference, "Invalid Origins Origin id '" + reference + "'");
            return Optional.empty();
        }
        Optional<Holder.Reference<Origin>> holder = registry.getHolder(id);
        if (holder.isEmpty()) warnOnce("origin:" + reference, "Unknown Origins Origin " + id);
        return holder;
    }

    private static <T> TagKey<T> parseTag(Registry<T> registry, String reference) {
        if (!reference.startsWith("#")) return null;
        ResourceLocation id = ResourceLocation.tryParse(reference.substring(1));
        if (id == null) {
            warnOnce("tag:" + reference, "Invalid Origins tag '" + reference + "'");
            return null;
        }
        return TagKey.create(registry.key(), id);
    }

    private static Optional<Set<Holder<Power>>> resolvePowers(Registry<Power> registry, String reference) {
        if (reference.startsWith("#")) {
            TagKey<Power> tag = parseTag(registry, reference);
            if (tag == null) return Optional.empty();
            Optional<net.minecraft.core.HolderSet.Named<Power>> holders = registry.getTag(tag);
            if (holders.isEmpty()) {
                warnOnce("power-tag:" + reference, "Unknown Origins Power tag " + reference);
                return Optional.empty();
            }
            Set<Holder<Power>> result = new HashSet<>();
            holders.get().forEach(result::add);
            return Optional.of(result);
        }
        ResourceLocation id = ResourceLocation.tryParse(reference);
        if (id == null) {
            warnOnce("power:" + reference, "Invalid Origins Power id '" + reference + "'");
            return Optional.empty();
        }
        Optional<Holder.Reference<Power>> holder = registry.getHolder(id);
        if (holder.isEmpty()) {
            warnOnce("power:" + reference, "Unknown Origins Power " + id);
            return Optional.empty();
        }
        return Optional.of(Set.of(holder.get()));
    }

    private static void onGrantOrigin(GrantOriginEvent event) {
        try {
            if (!(event.getEntity() instanceof ServerPlayer player) || player instanceof FakePlayer) return;
            event.getOrigin().unwrapKey().ifPresent(key -> {
                Registry<Origin> registry = player.server.registryAccess().registryOrThrow(OriginRegistries.ORIGIN_KEY);
                AptitudeSourceManager.grant(player, ORIGIN_GRANTED, key.location(), registry);
                ModuleManager.checkAutoUnlock(player);
                EpiphanyManager.checkAutoUnlock(player);
            });
        } catch (Exception e) {
            EpiphanyExtra.LOGGER.error("Origins Origin grant bridge failed", e);
        }
    }

    private static void onGrantPower(GrantPowerEvent event) {
        try {
            if (!(event.getEntity() instanceof ServerPlayer player) || player instanceof FakePlayer) return;
            event.getPower().unwrapKey().ifPresent(key -> {
                Registry<Power> registry = player.server.registryAccess().registryOrThrow(PowerRegistries.POWER_KEY);
                AptitudeSourceManager.grant(player, POWER_GRANTED, key.location(), registry);
                ModuleManager.checkAutoUnlock(player);
                EpiphanyManager.checkAutoUnlock(player);
            });
        } catch (Exception e) {
            EpiphanyExtra.LOGGER.error("Origins Power grant bridge failed", e);
        }
    }

    private static void warnOnce(String key, String message) {
        if (DIAGNOSTICS.add(key)) EpiphanyExtra.LOGGER.warn(message);
    }
}
