package ink.myumoon.epiphanyextra.viscriptteam.reward;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ink.myumoon.epiphany.content.reward.EpiphanyReward;
import ink.myumoon.epiphany.content.reward.InsightReward;
import ink.myumoon.epiphany.content.reward.RewardSource;
import ink.myumoon.epiphanyextra.viscriptteam.ViScriptTeamHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/** Adds source-tracked standing to a ViScriptTeam faction. */
public record ViScriptTeamStandingReward(String faction, int delta)
        implements InsightReward, EpiphanyReward {
    private static final Codec<Integer> NON_ZERO_DELTA = Codec.INT.flatXmap(
            value -> value != 0 ? DataResult.success(value)
                    : DataResult.error(() -> "ViScriptTeam standing reward delta cannot be zero"),
            value -> value != 0 ? DataResult.success(value)
                    : DataResult.error(() -> "ViScriptTeam standing reward delta cannot be zero"));

    public static final MapCodec<ViScriptTeamStandingReward> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("faction").forGetter(ViScriptTeamStandingReward::faction),
            NON_ZERO_DELTA.fieldOf("delta").forGetter(ViScriptTeamStandingReward::delta)
    ).apply(instance, ViScriptTeamStandingReward::new));

    @Override public MapCodec<? extends ViScriptTeamStandingReward> codec() { return CODEC; }
    @Override public void apply(ServerPlayer player, ResourceLocation sourceId) {
        apply(player, new RewardSource("legacy_api", sourceId, "reward", sourceId));
    }
    @Override public void apply(ServerPlayer player, RewardSource source) {
        ViScriptTeamHelper.applyStanding(player, faction, delta, source);
    }
    @Override public void remove(ServerPlayer player, ResourceLocation sourceId) {
        remove(player, new RewardSource("legacy_api", sourceId, "reward", sourceId));
    }
    @Override public void remove(ServerPlayer player, RewardSource source) {
        ViScriptTeamHelper.removeStanding(player, source);
    }
}
