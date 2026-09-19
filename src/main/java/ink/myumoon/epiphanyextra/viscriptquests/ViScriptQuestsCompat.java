package ink.myumoon.epiphanyextra.viscriptquests;

import ink.myumoon.epiphanyextra.viscriptquests.registry.ViScriptQuestsAttachments;
import ink.myumoon.epiphanyextra.viscriptquests.registry.ViScriptQuestsRegistries;
import net.neoforged.bus.api.IEventBus;

/** Lifecycle entry point for the optional ViScriptQuests integration. */
public final class ViScriptQuestsCompat {
    private ViScriptQuestsCompat() {}

    public static void init(IEventBus modEventBus) {
        ViScriptQuestsRegistries.register(modEventBus);
        ViScriptQuestsAttachments.register(modEventBus);
        ViScriptQuestsHelper.init();
    }
}
