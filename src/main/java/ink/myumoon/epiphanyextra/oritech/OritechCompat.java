package ink.myumoon.epiphanyextra.oritech;

import ink.myumoon.epiphanyextra.oritech.registry.OritechRegistries;
import net.neoforged.bus.api.IEventBus;

/** Lifecycle entry point for the optional Oritech integration. */
public final class OritechCompat {
    private OritechCompat() {}

    public static void init(IEventBus modEventBus) {
        OritechRegistries.register(modEventBus);
        OritechHelper.init();
    }
}
