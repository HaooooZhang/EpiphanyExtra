package ink.myumoon.epiphanyextra.fieldguide;

import ink.myumoon.epiphanyextra.fieldguide.registry.FieldGuideRegistries;
import net.neoforged.bus.api.IEventBus;

/** Lifecycle entry point for the optional Field Guide integration. */
public final class FieldGuideCompat {
    private FieldGuideCompat() {}

    public static void init(IEventBus modEventBus) {
        FieldGuideRegistries.register(modEventBus);
        FieldGuideHelper.init();
    }
}
