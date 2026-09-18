package ink.myumoon.epiphanyextra.reskillable.registry;

import com.mojang.serialization.MapCodec;
import ink.myumoon.epiphany.content.condition.Condition;
import ink.myumoon.epiphany.content.reward.EpiphanyReward;
import ink.myumoon.epiphany.content.reward.InsightReward;
import ink.myumoon.epiphany.registry.EpiphanyRegistries;
import ink.myumoon.epiphanyextra.EpiphanyExtra;
import ink.myumoon.epiphanyextra.reskillable.condition.ReskillableSkillLevelCondition;
import ink.myumoon.epiphanyextra.reskillable.reward.ReskillableSkillLevelReward;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Registry bindings owned by the Reskillable integration. */
public final class ReskillableRegistries {
    public static final DeferredRegister<MapCodec<? extends Condition>> CONDITIONS =
            DeferredRegister.create(EpiphanyRegistries.CONDITION_SERIALIZERS, EpiphanyExtra.MODID);
    public static final DeferredRegister<MapCodec<? extends InsightReward>> INSIGHT_REWARDS =
            DeferredRegister.create(EpiphanyRegistries.INSIGHT_REWARD_SERIALIZERS, EpiphanyExtra.MODID);
    public static final DeferredRegister<MapCodec<? extends EpiphanyReward>> EPIPHANY_REWARDS =
            DeferredRegister.create(EpiphanyRegistries.EPIPHANY_REWARD_SERIALIZERS, EpiphanyExtra.MODID);

    static {
        CONDITIONS.register("reskillable_skill_level", () -> ReskillableSkillLevelCondition.CODEC);
        INSIGHT_REWARDS.register("reskillable_skill_level", () -> ReskillableSkillLevelReward.CODEC);
        EPIPHANY_REWARDS.register("reskillable_skill_level", () -> ReskillableSkillLevelReward.CODEC);
    }

    private ReskillableRegistries() {}

    public static void register(IEventBus bus) {
        CONDITIONS.register(bus);
        INSIGHT_REWARDS.register(bus);
        EPIPHANY_REWARDS.register(bus);
    }
}
