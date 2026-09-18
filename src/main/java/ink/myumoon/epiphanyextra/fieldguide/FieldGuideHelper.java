package ink.myumoon.epiphanyextra.fieldguide;

import ink.myumoon.epiphanyextra.EpiphanyExtra;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;

/** Safe facade for the optional Field Guide integration. */
public final class FieldGuideHelper {
    private static final boolean LOADED = ModList.get().isLoaded("fieldguide");

    static {
        EpiphanyExtra.LOGGER.info("Field Guide compatibility {}",
                LOADED ? "enabled" : "disabled (Field Guide not loaded)");
    }

    private FieldGuideHelper() {}

    public static void init() {
        // Field Guide does not expose a public NeoForge entry-unlocked event.
        // Epiphany's periodic auto-unlock check evaluates this read-only condition.
    }

    public static boolean isEntityUnlocked(ServerPlayer player, ResourceLocation entityId) {
        return LOADED && FieldGuideInternal.isEntityUnlocked(player, entityId);
    }
}
