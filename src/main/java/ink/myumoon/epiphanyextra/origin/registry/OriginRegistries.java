package ink.myumoon.epiphanyextra.origin.registry;

import com.mojang.serialization.MapCodec;
import ink.myumoon.epiphany.content.condition.Condition;
import ink.myumoon.epiphany.content.reward.EpiphanyReward;
import ink.myumoon.epiphany.content.reward.InsightReward;
import ink.myumoon.epiphany.registry.EpiphanyRegistries;
import ink.myumoon.epiphanyextra.EpiphanyExtra;
import ink.myumoon.epiphanyextra.origin.condition.OriginsOriginCondition;
import ink.myumoon.epiphanyextra.origin.reward.OriginsPowerReward;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Registry bindings owned by the Origin integration. */
public final class OriginRegistries {
    public static final DeferredRegister<MapCodec<? extends Condition>> CONDITIONS =
            DeferredRegister.create(EpiphanyRegistries.CONDITION_SERIALIZERS, EpiphanyExtra.MODID);
    public static final DeferredRegister<MapCodec<? extends InsightReward>> INSIGHT_REWARDS =
            DeferredRegister.create(EpiphanyRegistries.INSIGHT_REWARD_SERIALIZERS, EpiphanyExtra.MODID);
    public static final DeferredRegister<MapCodec<? extends EpiphanyReward>> EPIPHANY_REWARDS =
            DeferredRegister.create(EpiphanyRegistries.EPIPHANY_REWARD_SERIALIZERS, EpiphanyExtra.MODID);

    static {
        CONDITIONS.register("origins_origin", () -> OriginsOriginCondition.CODEC);
        INSIGHT_REWARDS.register("origins_power", () -> OriginsPowerReward.CODEC);
        EPIPHANY_REWARDS.register("origins_power", () -> OriginsPowerReward.CODEC);
    }

    private OriginRegistries() {}

    public static void register(IEventBus bus) {
        CONDITIONS.register(bus);
        INSIGHT_REWARDS.register(bus);
        EPIPHANY_REWARDS.register(bus);
    }
}
