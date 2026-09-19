package ink.myumoon.epiphanyextra.viscriptquests.reward;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ink.myumoon.epiphany.content.reward.EpiphanyReward;
import ink.myumoon.epiphany.content.reward.InsightReward;
import ink.myumoon.epiphanyextra.viscriptquests.ViScriptQuestsHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/** Adds a quest to the player's ViScriptQuests flow. */
public record ViScriptQuestsGrantReward(String quest) implements InsightReward, EpiphanyReward {
    public static final MapCodec<ViScriptQuestsGrantReward> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("quest").forGetter(ViScriptQuestsGrantReward::quest)
    ).apply(instance, ViScriptQuestsGrantReward::new));

    @Override public MapCodec<? extends ViScriptQuestsGrantReward> codec() { return CODEC; }
    @Override public void apply(ServerPlayer player, ResourceLocation sourceId) {
        ViScriptQuestsHelper.grant(player, quest);
    }

    @Override public void remove(ServerPlayer player, ResourceLocation sourceId) {}
}
