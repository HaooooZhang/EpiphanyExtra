package ink.myumoon.epiphanyextra.viscriptquests;

/** Package bridge keeping the registry listener facade free of third-party types. */
public final class ViScriptQuestsInternalBridge {
    private ViScriptQuestsInternalBridge() {}

    public static void registerEvents() {
        ViScriptQuestsInternal.registerEvents();
    }
}
