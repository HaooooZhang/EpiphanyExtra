package ink.myumoon.epiphanyextra.viscriptquests.registry;

import com.mojang.serialization.MapCodec;
import ink.myumoon.epiphany.content.condition.Condition;
import ink.myumoon.epiphany.content.reward.EpiphanyReward;
import ink.myumoon.epiphany.content.reward.InsightReward;
import ink.myumoon.epiphany.registry.EpiphanyRegistries;
import ink.myumoon.epiphanyextra.EpiphanyExtra;
import ink.myumoon.epiphanyextra.viscriptquests.condition.ViScriptQuestsObjectiveCondition;
import ink.myumoon.epiphanyextra.viscriptquests.condition.ViScriptQuestsQuestCondition;
import ink.myumoon.epiphanyextra.viscriptquests.condition.ViScriptQuestsTaskCondition;
import ink.myumoon.epiphanyextra.viscriptquests.reward.ViScriptQuestsCompleteReward;
import ink.myumoon.epiphanyextra.viscriptquests.reward.ViScriptQuestsGrantReward;
import ink.myumoon.epiphanyextra.viscriptquests.reward.ViScriptQuestsTriggerReward;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ViScriptQuestsRegistries {
    public static final DeferredRegister<MapCodec<? extends Condition>> CONDITIONS =
            DeferredRegister.create(EpiphanyRegistries.CONDITION_SERIALIZERS, EpiphanyExtra.MODID);
    public static final DeferredRegister<MapCodec<? extends InsightReward>> INSIGHT_REWARDS =
            DeferredRegister.create(EpiphanyRegistries.INSIGHT_REWARD_SERIALIZERS, EpiphanyExtra.MODID);
    public static final DeferredRegister<MapCodec<? extends EpiphanyReward>> EPIPHANY_REWARDS =
            DeferredRegister.create(EpiphanyRegistries.EPIPHANY_REWARD_SERIALIZERS, EpiphanyExtra.MODID);

    static {
        CONDITIONS.register("viscript_quests_quest", () -> ViScriptQuestsQuestCondition.CODEC);
        CONDITIONS.register("viscript_quests_task", () -> ViScriptQuestsTaskCondition.CODEC);
        CONDITIONS.register("viscript_quests_objective", () -> ViScriptQuestsObjectiveCondition.CODEC);
        INSIGHT_REWARDS.register("viscript_quests_grant", () -> ViScriptQuestsGrantReward.CODEC);
        INSIGHT_REWARDS.register("viscript_quests_complete", () -> ViScriptQuestsCompleteReward.CODEC);
        INSIGHT_REWARDS.register("viscript_quests_trigger", () -> ViScriptQuestsTriggerReward.CODEC);
        EPIPHANY_REWARDS.register("viscript_quests_grant", () -> ViScriptQuestsGrantReward.CODEC);
        EPIPHANY_REWARDS.register("viscript_quests_complete", () -> ViScriptQuestsCompleteReward.CODEC);
        EPIPHANY_REWARDS.register("viscript_quests_trigger", () -> ViScriptQuestsTriggerReward.CODEC);
    }

    private ViScriptQuestsRegistries() {}

    public static void register(IEventBus bus) {
        CONDITIONS.register(bus);
        INSIGHT_REWARDS.register(bus);
        EPIPHANY_REWARDS.register(bus);
    }
}
