package ink.myumoon.epiphanyextra.origin;

import ink.myumoon.epiphanyextra.origin.registry.OriginRegistries;
import net.neoforged.bus.api.IEventBus;

/** Lifecycle entry point for the optional Origins integration. */
public final class OriginCompat {
    private OriginCompat() {}

    public static void init(IEventBus modEventBus) {
        OriginRegistries.register(modEventBus);
        OriginHelper.init();
    }
}
