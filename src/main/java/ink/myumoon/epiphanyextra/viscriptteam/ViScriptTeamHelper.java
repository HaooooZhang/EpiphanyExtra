package ink.myumoon.epiphanyextra.viscriptteam;

import ink.myumoon.epiphany.content.condition.Comparison;
import ink.myumoon.epiphany.content.reward.RewardSource;
import ink.myumoon.epiphanyextra.EpiphanyExtra;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;

/** Safe facade for the optional ViScriptTeam integration. */
public final class ViScriptTeamHelper {
    private static final boolean LOADED = ModList.get().isLoaded("viscript_team");

    static {
        EpiphanyExtra.LOGGER.info("ViScriptTeam compatibility {}",
                LOADED ? "enabled" : "disabled (ViScriptTeam not loaded)");
    }

    private ViScriptTeamHelper() {}

    public static void init() {
        // ViScriptTeam 1.0.2 beta exposes no stable standing/party change event.
    }

    public static boolean hasParty(ServerPlayer player, String party) {
        return LOADED && ViScriptTeamInternal.hasParty(player, party);
    }

    public static boolean hasStanding(ServerPlayer player, String faction,
                                      Comparison comparison, int points) {
        return LOADED && ViScriptTeamInternal.hasStanding(player, faction, comparison, points);
    }

    public static boolean hasAttitude(ServerPlayer player, String faction, String attitude) {
        return LOADED && ViScriptTeamInternal.hasAttitude(player, faction, attitude);
    }

    public static boolean hasPartyStanding(ServerPlayer player, String faction, String strategy,
                                           Comparison comparison, int points) {
        return LOADED && ViScriptTeamInternal.hasPartyStanding(player, faction, strategy, comparison, points);
    }

    public static boolean hasPartyAttitude(ServerPlayer player, String faction, String strategy,
                                           String attitude) {
        return LOADED && ViScriptTeamInternal.hasPartyAttitude(player, faction, strategy, attitude);
    }

    public static void applyStanding(ServerPlayer player, String faction, int delta, RewardSource source) {
        if (LOADED) ViScriptTeamInternal.applyStanding(player, faction, delta, source);
    }

    public static void removeStanding(ServerPlayer player, RewardSource source) {
        if (LOADED) ViScriptTeamInternal.removeStanding(player, source);
    }
}
