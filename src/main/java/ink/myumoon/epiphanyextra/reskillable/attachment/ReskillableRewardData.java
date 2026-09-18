package ink.myumoon.epiphanyextra.reskillable.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ink.myumoon.epiphany.content.reward.RewardSource;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

/** EpiphanyExtra-owned source ledger for Reskillable level rewards. */
public record ReskillableRewardData(Map<String, Grant> grants) {
    public static final ReskillableRewardData EMPTY = new ReskillableRewardData(new HashMap<>());

    public static final Codec<Grant> GRANT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("owner_kind").forGetter(Grant::ownerKind),
            ResourceLocation.CODEC.fieldOf("owner_id").forGetter(Grant::ownerId),
            Codec.STRING.fieldOf("reward_slot").forGetter(Grant::rewardSlot),
            ResourceLocation.CODEC.fieldOf("legacy_id").forGetter(Grant::legacyId),
            Codec.STRING.fieldOf("skill").forGetter(Grant::skill),
            Codec.INT.fieldOf("requested_levels").forGetter(Grant::requestedLevels),
            Codec.INT.fieldOf("granted_levels").forGetter(Grant::grantedLevels)
    ).apply(instance, Grant::new));

    public static final Codec<ReskillableRewardData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(Codec.STRING, GRANT_CODEC).optionalFieldOf("grants", new HashMap<>())
                    .forGetter(ReskillableRewardData::grants)
    ).apply(instance, ReskillableRewardData::new));

    public Grant get(RewardSource source) {
        return grants.get(sourceKey(source));
    }

    public ReskillableRewardData with(RewardSource source, Grant grant) {
        Map<String, Grant> copy = new HashMap<>(grants);
        copy.put(sourceKey(source), grant);
        return new ReskillableRewardData(copy);
    }

    public ReskillableRewardData without(RewardSource source) {
        Map<String, Grant> copy = new HashMap<>(grants);
        copy.remove(sourceKey(source));
        return new ReskillableRewardData(copy);
    }

    public static String sourceKey(RewardSource source) {
        return source.ownerKind() + "|" + source.ownerId() + "|" + source.rewardSlot()
                + "|" + source.legacyId();
    }

    public record Grant(String ownerKind, ResourceLocation ownerId, String rewardSlot,
                        ResourceLocation legacyId, String skill, int requestedLevels,
                        int grantedLevels) {
        public RewardSource source() {
            return new RewardSource(ownerKind, ownerId, rewardSlot, legacyId);
        }
    }
}
