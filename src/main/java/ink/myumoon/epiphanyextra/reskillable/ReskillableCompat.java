package ink.myumoon.epiphanyextra.reskillable;

import ink.myumoon.epiphanyextra.reskillable.registry.ReskillableAttachments;
import ink.myumoon.epiphanyextra.reskillable.registry.ReskillableRegistries;
import net.neoforged.bus.api.IEventBus;

/** Lifecycle entry point for the optional Reskillable integration. */
public final class ReskillableCompat {
    private ReskillableCompat() {}

    public static void init(IEventBus modEventBus) {
        ReskillableAttachments.register(modEventBus);
        ReskillableRegistries.register(modEventBus);
        ReskillableHelper.init();
    }
}
