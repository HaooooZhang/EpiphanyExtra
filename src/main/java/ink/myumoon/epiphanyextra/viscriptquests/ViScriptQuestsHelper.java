package ink.myumoon.epiphanyextra.viscriptquests;

import ink.myumoon.epiphany.content.condition.Comparison;
import ink.myumoon.epiphany.content.reward.RewardSource;
import ink.myumoon.epiphanyextra.EpiphanyExtra;
import ink.myumoon.epiphanyextra.viscriptquests.registry.ViScriptQuestsAptitudeListener;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;

import java.util.Optional;

/** Safe facade for the optional ViScriptQuests integration. */
public final class ViScriptQuestsHelper {
    private static final boolean LOADED = ModList.get().isLoaded("viscript_quests");

    static {
        EpiphanyExtra.LOGGER.info("ViScriptQuests compatibility {}",
                LOADED ? "enabled" : "disabled (ViScriptQuests not loaded)");
    }

    private ViScriptQuestsHelper() {}

    public static void init() {
        if (LOADED) ViScriptQuestsAptitudeListener.register();
    }

    public static boolean hasQuest(ServerPlayer player, String quest, String status) {
        return LOADED && ViScriptQuestsInternal.hasQuest(player, quest, status);
    }

    public static boolean hasTask(ServerPlayer player, String quest, String task, String status) {
        return LOADED && ViScriptQuestsInternal.hasTask(player, quest, task, status);
    }

    public static boolean hasObjective(ServerPlayer player, String quest, String task, String objective,
                                       Optional<String> status, Optional<Comparison> comparison,
                                       Optional<Integer> amount) {
        return LOADED && ViScriptQuestsInternal.hasObjective(player, quest, task, objective,
                status, comparison, amount);
    }

    public static boolean grant(ServerPlayer player, String quest) {
        return LOADED && ViScriptQuestsInternal.grant(player, quest);
    }

    public static boolean complete(ServerPlayer player, String quest) {
        return LOADED && ViScriptQuestsInternal.complete(player, quest);
    }

    public static boolean triggerCustom(ServerPlayer player, String trigger, RewardSource source) {
        return LOADED && ViScriptQuestsInternal.triggerCustom(player, trigger, source);
    }
}
