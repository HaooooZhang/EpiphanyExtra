package ink.myumoon.epiphanyextra.reskillable.reward;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ink.myumoon.epiphany.content.reward.EpiphanyReward;
import ink.myumoon.epiphany.content.reward.InsightReward;
import ink.myumoon.epiphany.content.reward.RewardSource;
import ink.myumoon.epiphanyextra.reskillable.ReskillableHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/** Adds and source-tracks Reskillable skill levels. */
public record ReskillableSkillLevelReward(String skill, int levels)
        implements InsightReward, EpiphanyReward {
    private static final Codec<Integer> POSITIVE_LEVELS = Codec.INT.flatXmap(
            value -> value >= 1 ? DataResult.success(value)
                    : DataResult.error(() -> "Reskillable reward levels must be positive"),
            value -> value >= 1 ? DataResult.success(value)
                    : DataResult.error(() -> "Reskillable reward levels must be positive"));

    public static final MapCodec<ReskillableSkillLevelReward> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("skill").forGetter(ReskillableSkillLevelReward::skill),
            POSITIVE_LEVELS.fieldOf("levels").forGetter(ReskillableSkillLevelReward::levels)
    ).apply(instance, ReskillableSkillLevelReward::new));

    @Override
    public MapCodec<? extends ReskillableSkillLevelReward> codec() {
        return CODEC;
    }

    @Override
    public void apply(ServerPlayer player, ResourceLocation sourceId) {
        apply(player, new RewardSource("legacy_api", sourceId, "reward", sourceId));
    }

    @Override
    public void apply(ServerPlayer player, RewardSource source) {
        ReskillableHelper.applySkillLevel(player, skill, levels, source);
    }

    @Override
    public void remove(ServerPlayer player, ResourceLocation sourceId) {
        remove(player, new RewardSource("legacy_api", sourceId, "reward", sourceId));
    }

    @Override
    public void remove(ServerPlayer player, RewardSource source) {
        ReskillableHelper.removeSkillLevel(player, source);
    }
}
