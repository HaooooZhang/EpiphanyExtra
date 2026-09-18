package ink.myumoon.epiphanyextra.viscriptteam.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ink.myumoon.epiphany.content.reward.RewardSource;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

/** EpiphanyExtra-owned source ledger for ViScriptTeam standing rewards. */
public record ViScriptTeamRewardData(Map<String, Grant> grants) {
    public static final ViScriptTeamRewardData EMPTY = new ViScriptTeamRewardData(new HashMap<>());

    public static final Codec<Grant> GRANT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("owner_kind").forGetter(Grant::ownerKind),
            ResourceLocation.CODEC.fieldOf("owner_id").forGetter(Grant::ownerId),
            Codec.STRING.fieldOf("reward_slot").forGetter(Grant::rewardSlot),
            ResourceLocation.CODEC.fieldOf("legacy_id").forGetter(Grant::legacyId),
            Codec.STRING.fieldOf("faction").forGetter(Grant::faction),
            Codec.INT.fieldOf("delta").forGetter(Grant::delta)
    ).apply(instance, Grant::new));

    public static final Codec<ViScriptTeamRewardData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(Codec.STRING, GRANT_CODEC).optionalFieldOf("grants", new HashMap<>())
                    .forGetter(ViScriptTeamRewardData::grants)
    ).apply(instance, ViScriptTeamRewardData::new));

    public Grant get(RewardSource source) { return grants.get(sourceKey(source)); }

    public ViScriptTeamRewardData with(RewardSource source, Grant grant) {
        Map<String, Grant> copy = new HashMap<>(grants);
        copy.put(sourceKey(source), grant);
        return new ViScriptTeamRewardData(copy);
    }

    public ViScriptTeamRewardData without(RewardSource source) {
        Map<String, Grant> copy = new HashMap<>(grants);
        copy.remove(sourceKey(source));
        return new ViScriptTeamRewardData(copy);
    }

    private static String sourceKey(RewardSource source) {
        return source.ownerKind() + "|" + source.ownerId() + "|" + source.rewardSlot()
                + "|" + source.legacyId();
    }

    public record Grant(String ownerKind, ResourceLocation ownerId, String rewardSlot,
                        ResourceLocation legacyId, String faction, int delta) {
        public RewardSource source() { return new RewardSource(ownerKind, ownerId, rewardSlot, legacyId); }
    }
}
