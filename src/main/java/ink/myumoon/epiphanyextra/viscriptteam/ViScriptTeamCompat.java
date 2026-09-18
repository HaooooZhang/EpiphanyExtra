package ink.myumoon.epiphanyextra.viscriptteam;

import ink.myumoon.epiphanyextra.viscriptteam.registry.ViScriptTeamAttachments;
import ink.myumoon.epiphanyextra.viscriptteam.registry.ViScriptTeamRegistries;
import net.neoforged.bus.api.IEventBus;

/** Lifecycle entry point for the optional ViScriptTeam integration. */
public final class ViScriptTeamCompat {
    private ViScriptTeamCompat() {}

    public static void init(IEventBus modEventBus) {
        ViScriptTeamRegistries.register(modEventBus);
        ViScriptTeamAttachments.register(modEventBus);
        ViScriptTeamHelper.init();
    }
}
