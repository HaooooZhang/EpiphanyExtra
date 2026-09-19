package ink.myumoon.epiphanyextra.viscriptquests.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ink.myumoon.epiphany.content.reward.RewardSource;

import java.util.HashMap;
import java.util.Map;

/** EpiphanyExtra-owned source ledger for one-shot custom trigger rewards. */
public record ViScriptQuestsRewardData(Map<String, Grant> grants) {
    public static final ViScriptQuestsRewardData EMPTY = new ViScriptQuestsRewardData(new HashMap<>());

    public static final Codec<Grant> GRANT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("owner_kind").forGetter(Grant::ownerKind),
            net.minecraft.resources.ResourceLocation.CODEC.fieldOf("owner_id").forGetter(Grant::ownerId),
            Codec.STRING.fieldOf("reward_slot").forGetter(Grant::rewardSlot),
            net.minecraft.resources.ResourceLocation.CODEC.fieldOf("legacy_id").forGetter(Grant::legacyId),
            Codec.STRING.fieldOf("trigger").forGetter(Grant::trigger)
    ).apply(instance, Grant::new));

    public static final Codec<ViScriptQuestsRewardData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(Codec.STRING, GRANT_CODEC).optionalFieldOf("grants", new HashMap<>())
                    .forGetter(ViScriptQuestsRewardData::grants)
    ).apply(instance, ViScriptQuestsRewardData::new));

    public boolean has(RewardSource source) {
        return grants.containsKey(sourceKey(source));
    }

    public ViScriptQuestsRewardData with(RewardSource source, String trigger) {
        Map<String, Grant> copy = new HashMap<>(grants);
        copy.put(sourceKey(source), new Grant(source.ownerKind(), source.ownerId(), source.rewardSlot(),
                source.legacyId(), trigger));
        return new ViScriptQuestsRewardData(copy);
    }

    private static String sourceKey(RewardSource source) {
        return source.ownerKind() + "|" + source.ownerId() + "|" + source.rewardSlot()
                + "|" + source.legacyId();
    }

    public record Grant(String ownerKind, net.minecraft.resources.ResourceLocation ownerId,
                        String rewardSlot, net.minecraft.resources.ResourceLocation legacyId,
                        String trigger) {}
}
