package ink.myumoon.epiphanyextra.viscriptteam.registry;

import ink.myumoon.epiphanyextra.EpiphanyExtra;
import ink.myumoon.epiphanyextra.viscriptteam.attachment.ViScriptTeamRewardData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ViScriptTeamAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, EpiphanyExtra.MODID);
    public static final java.util.function.Supplier<AttachmentType<ViScriptTeamRewardData>> REWARDS =
            ATTACHMENTS.register("viscript_team_rewards", () -> AttachmentType.builder(
                    () -> ViScriptTeamRewardData.EMPTY)
                    .serialize(ViScriptTeamRewardData.CODEC)
                    .copyOnDeath()
                    .build());

    private ViScriptTeamAttachments() {}
    public static void register(IEventBus bus) { ATTACHMENTS.register(bus); }
}
