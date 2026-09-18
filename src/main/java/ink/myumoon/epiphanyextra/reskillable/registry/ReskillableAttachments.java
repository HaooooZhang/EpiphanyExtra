package ink.myumoon.epiphanyextra.reskillable.registry;

import ink.myumoon.epiphanyextra.EpiphanyExtra;
import ink.myumoon.epiphanyextra.reskillable.attachment.ReskillableRewardData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

/** Attachment registration for EpiphanyExtra-owned Reskillable reward sources. */
public final class ReskillableAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, EpiphanyExtra.MODID);

    public static final java.util.function.Supplier<AttachmentType<ReskillableRewardData>> RESKILLABLE_REWARDS =
            ATTACHMENTS.register("reskillable_rewards", () -> AttachmentType.builder(
                    () -> ReskillableRewardData.EMPTY)
                    .serialize(ReskillableRewardData.CODEC)
                    .copyOnDeath()
                    .build());

    private ReskillableAttachments() {}

    public static void register(IEventBus bus) {
        ATTACHMENTS.register(bus);
    }
}
