package ink.myumoon.epiphanyextra.ars.registry;

import com.mojang.serialization.MapCodec;
import ink.myumoon.epiphany.content.condition.Condition;
import ink.myumoon.epiphany.registry.EpiphanyRegistries;
import ink.myumoon.epiphanyextra.EpiphanyExtra;
import ink.myumoon.epiphanyextra.ars.condition.ArsGlyphCondition;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Registry bindings owned by the Ars Nouveau integration. */
public final class ArsRegistries {
    public static final DeferredRegister<MapCodec<? extends Condition>> CONDITIONS =
            DeferredRegister.create(EpiphanyRegistries.CONDITION_SERIALIZERS, EpiphanyExtra.MODID);

    static {
        CONDITIONS.register("ars_glyph", () -> ArsGlyphCondition.CODEC);
    }

    private ArsRegistries() {}

    public static void register(IEventBus modEventBus) {
        CONDITIONS.register(modEventBus);
    }
}
