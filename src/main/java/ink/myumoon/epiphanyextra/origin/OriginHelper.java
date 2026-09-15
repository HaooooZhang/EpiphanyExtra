package ink.myumoon.epiphanyextra.origin;

import ink.myumoon.epiphanyextra.EpiphanyExtra;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

/** Safe facade for the optional Origins integration. */
public final class OriginHelper {
    private static final boolean LOADED = ModList.get().isLoaded("origins");

    static {
        EpiphanyExtra.LOGGER.info("Origins compatibility {}", LOADED ? "enabled" : "disabled (Origins not loaded)");
    }

    private OriginHelper() {}

    public static boolean isLoaded() {
        return LOADED;
    }

    public static void init() {
        if (LOADED) OriginInternal.registerEvents();
    }

    public static boolean hasOrigin(ServerPlayer player, String reference, Optional<ResourceLocation> layer) {
        if (!LOADED) return false;
        return LOADED && OriginInternal.hasOrigin(player, reference, layer);
    }

    public static void grantPower(ServerPlayer player, String reference, ResourceLocation sourceId) {
        if (LOADED) OriginInternal.grantPower(player, reference, rewardSource(sourceId, reference));
    }

    public static void revokePower(ServerPlayer player, String reference, ResourceLocation sourceId) {
        if (LOADED) OriginInternal.revokePower(player, reference, rewardSource(sourceId, reference));
    }

    private static ResourceLocation rewardSource(ResourceLocation sourceId, String expression) {
        return ResourceLocation.fromNamespaceAndPath(
                EpiphanyExtra.MODID,
                "reward/" + sourceId.getNamespace() + "/" + sourceId.getPath()
                        + "/target_" + encodeExpression(expression));
    }

    /**
     * Keeps the target expression in the derived source while producing a
     * ResourceLocation-safe, lowercase and collision-free path component.
     */
    private static String encodeExpression(String expression) {
        byte[] bytes = expression.getBytes(StandardCharsets.UTF_8);
        StringBuilder encoded = new StringBuilder(bytes.length * 2);
        for (byte value : bytes) {
            encoded.append(String.format("%02x", value & 0xff));
        }
        return encoded.toString();
    }
}
