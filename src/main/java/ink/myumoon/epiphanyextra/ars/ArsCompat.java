package ink.myumoon.epiphanyextra.ars;

import ink.myumoon.epiphanyextra.ars.registry.ArsRegistries;
import net.neoforged.bus.api.IEventBus;

/** Lifecycle entry point for the optional Ars Nouveau integration. */
public final class ArsCompat {
    private ArsCompat() {}

    public static void init(IEventBus modEventBus) {
        ArsRegistries.register(modEventBus);
        ArsHelper.init();
    }
}
