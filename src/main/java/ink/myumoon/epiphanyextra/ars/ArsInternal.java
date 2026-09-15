package ink.myumoon.epiphanyextra.ars;

import com.hollingsworth.arsnouveau.api.registry.GlyphRegistry;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.common.capability.ANPlayerDataCap;
import com.hollingsworth.arsnouveau.setup.registry.CapabilityRegistry;
import ink.myumoon.epiphanyextra.EpiphanyExtra;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/** The only class in EpiphanyExtra that directly references Ars Nouveau classes. */
final class ArsInternal {
    private static final Set<ResourceLocation> UNKNOWN_GLYPHS = ConcurrentHashMap.newKeySet();
    private static final Set<String> DIAGNOSTICS = ConcurrentHashMap.newKeySet();

    private ArsInternal() {}

    static boolean knowsGlyph(ServerPlayer player, ResourceLocation glyphId) {
        try {
            AbstractSpellPart glyph = GlyphRegistry.getSpellPart(glyphId);
            if (glyph == null) {
                if (UNKNOWN_GLYPHS.add(glyphId)) {
                    EpiphanyExtra.LOGGER.warn("Unknown Ars Nouveau Glyph {}", glyphId);
                }
                return false;
            }

            ANPlayerDataCap playerData = CapabilityRegistry.getPlayerDataCap(player);
            if (playerData == null) {
                if (DIAGNOSTICS.add("player_data")) {
                    EpiphanyExtra.LOGGER.warn("Ars Nouveau player Glyph capability is unavailable");
                }
                return false;
            }
            // Compare registry ids instead of relying on AbstractSpellPart object
            // identity/equality. Ars can rebuild registry objects during a data
            // reload, while the player capability may still contain instances
            // created before that reload.
            boolean known = playerData.getKnownGlyphs().stream()
                    .filter(knownGlyph -> knownGlyph != null)
                    .anyMatch(knownGlyph -> glyphId.equals(knownGlyph.getRegistryName()));

            return known;
        } catch (RuntimeException exception) {
            EpiphanyExtra.LOGGER.warn("Failed to evaluate Ars Nouveau Glyph {}", glyphId, exception);
            return false;
        }
    }
}
