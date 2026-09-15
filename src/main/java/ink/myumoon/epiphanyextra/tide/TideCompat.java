package ink.myumoon.epiphanyextra.tide;

import ink.myumoon.epiphanyextra.tide.registry.TideRegistries;
import net.neoforged.bus.api.IEventBus;

/** Lifecycle entry point for the optional Tide integration. */
public final class TideCompat {
    private TideCompat() {}

    public static void init(IEventBus modEventBus) {
        TideRegistries.register(modEventBus);
        TideHelper.init();
    }
}
