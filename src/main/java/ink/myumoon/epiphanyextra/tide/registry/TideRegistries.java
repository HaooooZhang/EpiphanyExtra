package ink.myumoon.epiphanyextra.tide.registry;

import com.mojang.serialization.MapCodec;
import ink.myumoon.epiphany.content.condition.Condition;
import ink.myumoon.epiphany.registry.EpiphanyRegistries;
import ink.myumoon.epiphanyextra.EpiphanyExtra;
import ink.myumoon.epiphanyextra.tide.condition.TideFishCaughtCondition;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Registry bindings owned by the Tide integration. */
public final class TideRegistries {
    public static final DeferredRegister<MapCodec<? extends Condition>> CONDITIONS =
            DeferredRegister.create(EpiphanyRegistries.CONDITION_SERIALIZERS, EpiphanyExtra.MODID);

    static {
        CONDITIONS.register("tide_fish_caught", () -> TideFishCaughtCondition.CODEC);
    }

    private TideRegistries() {}

    public static void register(IEventBus modEventBus) {
        CONDITIONS.register(modEventBus);
    }
}
