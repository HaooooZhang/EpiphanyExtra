package ink.myumoon.epiphanyextra.reskillable;

import ink.myumoon.epiphany.content.condition.Comparison;
import ink.myumoon.epiphany.content.reward.RewardSource;
import ink.myumoon.epiphanyextra.EpiphanyExtra;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;

/** Safe facade for the optional Reskillable integration. */
public final class ReskillableHelper {
    private static final boolean LOADED = ModList.get().isLoaded("reskillable");

    static {
        EpiphanyExtra.LOGGER.info("Reskillable compatibility {}",
                LOADED ? "enabled" : "disabled (Reskillable not loaded)");
    }

    private ReskillableHelper() {}

    public static void init() {
        // Reskillable 4.0.5 does not expose a post-level-up event. No listener
        // or polling bridge is registered by this integration.
    }

    public static boolean hasSkillLevel(ServerPlayer player, String skill,
                                        Comparison comparison, int level) {
        return LOADED && ReskillableInternal.hasSkillLevel(player, skill, comparison, level);
    }

    public static void applySkillLevel(ServerPlayer player, String skill, int levels,
                                       RewardSource source) {
        if (LOADED) ReskillableInternal.applySkillLevel(player, skill, levels, source);
    }

    public static void removeSkillLevel(ServerPlayer player, RewardSource source) {
        if (LOADED) ReskillableInternal.removeSkillLevel(player, source);
    }
}
