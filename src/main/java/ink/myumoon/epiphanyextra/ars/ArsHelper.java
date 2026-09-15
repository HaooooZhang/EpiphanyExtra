package ink.myumoon.epiphanyextra.ars;

import ink.myumoon.epiphanyextra.EpiphanyExtra;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;

/** Safe facade for the optional Ars Nouveau integration. */
public final class ArsHelper {
    private static final boolean LOADED = ModList.get().isLoaded("ars_nouveau");

    static {
        EpiphanyExtra.LOGGER.info("Ars Nouveau compatibility {}",
                LOADED ? "enabled" : "disabled (Ars Nouveau not loaded)");
    }

    private ArsHelper() {}

    public static void init() {
        // Ars exposes no public Glyph-unlock event required by v1.
    }

    public static boolean knowsGlyph(ServerPlayer player, ResourceLocation glyphId) {
        return LOADED && ArsInternal.knowsGlyph(player, glyphId);
    }
}
