package ink.myumoon.epiphanyextra.viscriptquests.registry;

import ink.myumoon.epiphanyextra.viscriptquests.ViScriptQuestsInternalBridge;

/** Registers the optional ViScriptQuests lifecycle-to-aptitude bridge. */
public final class ViScriptQuestsAptitudeListener {
    private ViScriptQuestsAptitudeListener() {}

    public static void register() {
        ViScriptQuestsInternalBridge.registerEvents();
    }
}
