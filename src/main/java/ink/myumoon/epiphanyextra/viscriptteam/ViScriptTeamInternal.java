package ink.myumoon.epiphanyextra.viscriptteam;

import com.viscript_team.data.faction.Faction;
import com.viscript_team.data.faction.FactionAttitude;
import com.viscript_team.data.party.Party;
import com.viscript_team.data.party.PartyStandingStrategy;
import com.viscript_team.util.FactionApi;
import ink.myumoon.epiphany.content.condition.Comparison;
import ink.myumoon.epiphany.content.reward.RewardSource;
import ink.myumoon.epiphanyextra.EpiphanyExtra;
import ink.myumoon.epiphanyextra.viscriptteam.attachment.ViScriptTeamRewardData;
import ink.myumoon.epiphanyextra.viscriptteam.registry.ViScriptTeamAttachments;
import net.minecraft.server.level.ServerPlayer;

import java.util.Locale;
import java.util.Optional;

/** The only class in EpiphanyExtra that directly references ViScriptTeam types. */
final class ViScriptTeamInternal {
    private ViScriptTeamInternal() {}

    static boolean hasParty(ServerPlayer player, String partyId) {
        try {
            String normalized = Party.normalizeId(partyId);
            if (!FactionApi.data(player.serverLevel()).getParty(normalized).isPresent()) return false;
            return FactionApi.getPlayerPartyId(player).map(normalized::equals).orElse(false);
        } catch (RuntimeException exception) {
            EpiphanyExtra.LOGGER.warn("Failed to evaluate ViScriptTeam party {}", partyId, exception);
            return false;
        }
    }

    static boolean hasStanding(ServerPlayer player, String factionId,
                               Comparison comparison, int expected) {
        try {
            String faction = normalizeFaction(factionId);
            if (!factionExists(player, faction)) return false;
            return comparison.test(FactionApi.getPlayerStanding(player, faction), expected);
        } catch (RuntimeException exception) {
            EpiphanyExtra.LOGGER.warn("Failed to evaluate ViScriptTeam standing {}", factionId, exception);
            return false;
        }
    }

    static boolean hasAttitude(ServerPlayer player, String factionId, String expected) {
        try {
            String faction = normalizeFaction(factionId);
            if (!factionExists(player, faction)) return false;
            return attitudeId(FactionApi.getPlayerAttitude(player, faction)).equals(normalizeAttitude(expected));
        } catch (RuntimeException exception) {
            EpiphanyExtra.LOGGER.warn("Failed to evaluate ViScriptTeam attitude {}", factionId, exception);
            return false;
        }
    }

    static boolean hasPartyStanding(ServerPlayer player, String factionId, String strategyId,
                                    Comparison comparison, int expected) {
        try {
            String faction = normalizeFaction(factionId);
            if (!factionExists(player, faction)) return false;
            PartyStandingStrategy strategy = strategy(strategyId);
            return comparison.test(FactionApi.getPlayerEffectiveStanding(player, faction, strategy), expected);
        } catch (RuntimeException exception) {
            EpiphanyExtra.LOGGER.warn("Failed to evaluate ViScriptTeam party standing {}", factionId, exception);
            return false;
        }
    }

    static boolean hasPartyAttitude(ServerPlayer player, String factionId, String strategyId,
                                    String expected) {
        try {
            String faction = normalizeFaction(factionId);
            if (!factionExists(player, faction)) return false;
            PartyStandingStrategy strategy = strategy(strategyId);
            return attitudeId(FactionApi.getPlayerEffectiveAttitude(player, faction, strategy))
                    .equals(normalizeAttitude(expected));
        } catch (RuntimeException exception) {
            EpiphanyExtra.LOGGER.warn("Failed to evaluate ViScriptTeam party attitude {}", factionId, exception);
            return false;
        }
    }

    static void applyStanding(ServerPlayer player, String factionId, int delta, RewardSource source) {
        try {
            String faction = normalizeFaction(factionId);
            if (!factionExists(player, faction) || delta == 0) return;
            ViScriptTeamRewardData data = player.getData(ViScriptTeamAttachments.REWARDS);
            ViScriptTeamRewardData.Grant existing = data.get(source);
            if (existing != null && existing.faction().equals(faction) && existing.delta() == delta) return;
            if (existing != null) {
                FactionApi.addPlayerStanding(player, existing.faction(), -existing.delta());
                data = data.without(source);
            }
            FactionApi.addPlayerStanding(player, faction, delta);
            player.setData(ViScriptTeamAttachments.REWARDS,
                    data.with(source, new ViScriptTeamRewardData.Grant(
                            source.ownerKind(), source.ownerId(), source.rewardSlot(), source.legacyId(),
                            faction, delta)));
        } catch (RuntimeException exception) {
            EpiphanyExtra.LOGGER.warn("Failed to apply ViScriptTeam standing reward {}", factionId, exception);
        }
    }

    static void removeStanding(ServerPlayer player, RewardSource source) {
        try {
            ViScriptTeamRewardData data = player.getData(ViScriptTeamAttachments.REWARDS);
            ViScriptTeamRewardData.Grant grant = data.get(source);
            if (grant == null) return;
            if (factionExists(player, grant.faction())) {
                FactionApi.addPlayerStanding(player, grant.faction(), -grant.delta());
            }
            player.setData(ViScriptTeamAttachments.REWARDS, data.without(source));
        } catch (RuntimeException exception) {
            EpiphanyExtra.LOGGER.warn("Failed to remove ViScriptTeam standing reward", exception);
        }
    }

    private static boolean factionExists(ServerPlayer player, String faction) {
        return !faction.isEmpty() && FactionApi.data(player.serverLevel()).getFaction(faction).isPresent();
    }

    private static String normalizeFaction(String id) {
        return Faction.normalizeId(id);
    }

    private static String normalizeAttitude(String id) {
        return id == null ? "" : id.trim().toLowerCase(Locale.ROOT);
    }

    private static String attitudeId(FactionAttitude attitude) {
        return attitude.name().toLowerCase(Locale.ROOT);
    }

    private static PartyStandingStrategy strategy(String id) {
        return PartyStandingStrategy.byName(id == null ? "" : id.trim().toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new IllegalArgumentException("Unknown ViScriptTeam party strategy: " + id));
    }
}
