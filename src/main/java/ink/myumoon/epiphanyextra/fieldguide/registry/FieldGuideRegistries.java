package ink.myumoon.epiphanyextra.fieldguide.registry;

import com.mojang.serialization.MapCodec;
import ink.myumoon.epiphany.content.condition.Condition;
import ink.myumoon.epiphany.registry.EpiphanyRegistries;
import ink.myumoon.epiphanyextra.EpiphanyExtra;
import ink.myumoon.epiphanyextra.fieldguide.condition.FieldGuideEntityUnlockedCondition;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Registry bindings owned by the Field Guide integration. */
public final class FieldGuideRegistries {
    public static final DeferredRegister<MapCodec<? extends Condition>> CONDITIONS =
            DeferredRegister.create(EpiphanyRegistries.CONDITION_SERIALIZERS, EpiphanyExtra.MODID);

    static {
        CONDITIONS.register("fieldguide_entity_unlocked", () -> FieldGuideEntityUnlockedCondition.CODEC);
    }

    private FieldGuideRegistries() {}

    public static void register(IEventBus modEventBus) {
        CONDITIONS.register(modEventBus);
    }
}
