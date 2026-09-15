package ink.myumoon.epiphanyextra.oritech.registry;

import com.mojang.serialization.MapCodec;
import ink.myumoon.epiphany.content.condition.Condition;
import ink.myumoon.epiphany.registry.EpiphanyRegistries;
import ink.myumoon.epiphanyextra.EpiphanyExtra;
import ink.myumoon.epiphanyextra.oritech.condition.OritechAugmentCondition;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Registry bindings owned by the Oritech integration. */
public final class OritechRegistries {
    public static final DeferredRegister<MapCodec<? extends Condition>> CONDITIONS =
            DeferredRegister.create(EpiphanyRegistries.CONDITION_SERIALIZERS, EpiphanyExtra.MODID);

    static {
        CONDITIONS.register("oritech_augment", () -> OritechAugmentCondition.CODEC);
    }

    private OritechRegistries() {}

    public static void register(IEventBus modEventBus) {
        CONDITIONS.register(modEventBus);
    }
}
