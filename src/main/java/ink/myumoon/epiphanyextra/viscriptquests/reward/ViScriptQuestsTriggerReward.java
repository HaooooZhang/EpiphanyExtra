package ink.myumoon.epiphanyextra.viscriptquests.reward;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ink.myumoon.epiphany.content.reward.EpiphanyReward;
import ink.myumoon.epiphany.content.reward.InsightReward;
import ink.myumoon.epiphany.content.reward.RewardSource;
import ink.myumoon.epiphanyextra.viscriptquests.ViScriptQuestsHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/** Fires one matching custom quest trigger per Epiphany reward source. */
public record ViScriptQuestsTriggerReward(String trigger) implements InsightReward, EpiphanyReward {
    public static final MapCodec<ViScriptQuestsTriggerReward> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("trigger").forGetter(ViScriptQuestsTriggerReward::trigger)
    ).apply(instance, ViScriptQuestsTriggerReward::new));

    @Override public MapCodec<? extends ViScriptQuestsTriggerReward> codec() { return CODEC; }

    @Override public void apply(ServerPlayer player, ResourceLocation sourceId) {
        apply(player, new RewardSource("legacy_api", sourceId, "reward", sourceId));
    }

    @Override public void apply(ServerPlayer player, RewardSource source) {
        ViScriptQuestsHelper.triggerCustom(player, trigger, source);
    }

    @Override public void remove(ServerPlayer player, ResourceLocation sourceId) {}
}
