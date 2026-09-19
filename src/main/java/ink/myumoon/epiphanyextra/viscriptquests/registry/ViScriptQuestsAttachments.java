package ink.myumoon.epiphanyextra.viscriptquests.registry;

import ink.myumoon.epiphanyextra.EpiphanyExtra;
import ink.myumoon.epiphanyextra.viscriptquests.attachment.ViScriptQuestsRewardData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ViScriptQuestsAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, EpiphanyExtra.MODID);
    public static final java.util.function.Supplier<AttachmentType<ViScriptQuestsRewardData>> REWARDS =
            ATTACHMENTS.register("viscript_quests_rewards", () -> AttachmentType.builder(
                            () -> ViScriptQuestsRewardData.EMPTY)
                    .serialize(ViScriptQuestsRewardData.CODEC)
                    .copyOnDeath()
                    .build());

    private ViScriptQuestsAttachments() {}
    public static void register(IEventBus bus) { ATTACHMENTS.register(bus); }
}
