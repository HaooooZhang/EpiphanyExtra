package ink.myumoon.epiphanyextra.tide;

import ink.myumoon.epiphany.content.condition.Comparison;
import ink.myumoon.epiphanyextra.EpiphanyExtra;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;

/** Safe facade for the optional Tide integration. */
public final class TideHelper {
    private static final boolean LOADED = ModList.get().isLoaded("tide");

    static {
        EpiphanyExtra.LOGGER.info("Tide compatibility {}",
                LOADED ? "enabled" : "disabled (Tide not loaded)");
    }

    private TideHelper() {}

    public static void init() {
        // Tide has no event bridge in v1. Epiphany's periodic auto-unlock
        // check evaluates this read-only condition after player state changes.
    }

    public static boolean hasCaught(ServerPlayer player, ResourceLocation fish,
                                    Comparison comparison, int count) {
        return LOADED && TideInternal.hasCaught(player, fish, comparison, count);
    }
}
