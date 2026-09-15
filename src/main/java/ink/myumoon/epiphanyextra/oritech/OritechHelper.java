package ink.myumoon.epiphanyextra.oritech;

import ink.myumoon.epiphanyextra.EpiphanyExtra;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;

/** Safe facade for the optional Oritech integration. */
public final class OritechHelper {
    private static final boolean LOADED = ModList.get().isLoaded("oritech");

    static {
        EpiphanyExtra.LOGGER.info("Oritech compatibility {}",
                LOADED ? "enabled" : "disabled (Oritech not loaded)");
    }

    private OritechHelper() {}

    public static void init() {
        // Oritech exposes no public augment state-change event needed by v1.
    }

    public static boolean hasAugment(ServerPlayer player, ResourceLocation augmentId, boolean enabledOnly) {
        return LOADED && OritechInternal.hasAugment(player, augmentId, enabledOnly);
    }
}
