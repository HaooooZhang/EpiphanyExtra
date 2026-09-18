package ink.myumoon.epiphanyextra.viscriptteam.registry;

import com.mojang.serialization.MapCodec;
import ink.myumoon.epiphany.content.condition.Condition;
import ink.myumoon.epiphany.content.reward.EpiphanyReward;
import ink.myumoon.epiphany.content.reward.InsightReward;
import ink.myumoon.epiphany.registry.EpiphanyRegistries;
import ink.myumoon.epiphanyextra.EpiphanyExtra;
import ink.myumoon.epiphanyextra.viscriptteam.condition.*;
import ink.myumoon.epiphanyextra.viscriptteam.reward.ViScriptTeamStandingReward;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ViScriptTeamRegistries {
    public static final DeferredRegister<MapCodec<? extends Condition>> CONDITIONS =
            DeferredRegister.create(EpiphanyRegistries.CONDITION_SERIALIZERS, EpiphanyExtra.MODID);
    public static final DeferredRegister<MapCodec<? extends InsightReward>> INSIGHT_REWARDS =
            DeferredRegister.create(EpiphanyRegistries.INSIGHT_REWARD_SERIALIZERS, EpiphanyExtra.MODID);
    public static final DeferredRegister<MapCodec<? extends EpiphanyReward>> EPIPHANY_REWARDS =
            DeferredRegister.create(EpiphanyRegistries.EPIPHANY_REWARD_SERIALIZERS, EpiphanyExtra.MODID);

    static {
        CONDITIONS.register("viscript_team_party", () -> ViScriptTeamPartyCondition.CODEC);
        CONDITIONS.register("viscript_team_standing", () -> ViScriptTeamStandingCondition.CODEC);
        CONDITIONS.register("viscript_team_attitude", () -> ViScriptTeamAttitudeCondition.CODEC);
        CONDITIONS.register("viscript_team_party_standing", () -> ViScriptTeamPartyStandingCondition.CODEC);
        CONDITIONS.register("viscript_team_party_attitude", () -> ViScriptTeamPartyAttitudeCondition.CODEC);
        INSIGHT_REWARDS.register("viscript_team_standing", () -> ViScriptTeamStandingReward.CODEC);
        EPIPHANY_REWARDS.register("viscript_team_standing", () -> ViScriptTeamStandingReward.CODEC);
    }

    private ViScriptTeamRegistries() {}
    public static void register(IEventBus bus) {
        CONDITIONS.register(bus);
        INSIGHT_REWARDS.register(bus);
        EPIPHANY_REWARDS.register(bus);
    }
}
