package ink.myumoon.epiphanyextra.viscriptquests.reward;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ink.myumoon.epiphany.content.reward.EpiphanyReward;
import ink.myumoon.epiphany.content.reward.InsightReward;
import ink.myumoon.epiphanyextra.viscriptquests.ViScriptQuestsHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/** Forces a quest through ViScriptQuests' normal completion flow. */
public record ViScriptQuestsCompleteReward(String quest) implements InsightReward, EpiphanyReward {
    public static final MapCodec<ViScriptQuestsCompleteReward> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("quest").forGetter(ViScriptQuestsCompleteReward::quest)
    ).apply(instance, ViScriptQuestsCompleteReward::new));

    @Override public MapCodec<? extends ViScriptQuestsCompleteReward> codec() { return CODEC; }
    @Override public void apply(ServerPlayer player, ResourceLocation sourceId) {
        ViScriptQuestsHelper.complete(player, quest);
    }

    @Override public void remove(ServerPlayer player, ResourceLocation sourceId) {}
}
