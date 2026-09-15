package ink.myumoon.epiphanyextra.origin.reward;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ink.myumoon.epiphany.content.reward.EpiphanyReward;
import ink.myumoon.epiphany.content.reward.InsightReward;
import ink.myumoon.epiphany.content.reward.PersistentReward;
import ink.myumoon.epiphanyextra.origin.OriginHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/** Grants and revokes an Origins Power (or all Powers in a tag). */
public record OriginsPowerReward(String power) implements InsightReward, EpiphanyReward, PersistentReward {
    public static final MapCodec<OriginsPowerReward> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("power").forGetter(OriginsPowerReward::power)
    ).apply(instance, OriginsPowerReward::new));

    @Override
    public MapCodec<? extends OriginsPowerReward> codec() {
        return CODEC;
    }

    @Override
    public void apply(ServerPlayer player, ResourceLocation sourceId) {
        OriginHelper.grantPower(player, power, sourceId);
    }

    @Override
    public void remove(ServerPlayer player, ResourceLocation sourceId) {
        OriginHelper.revokePower(player, power, sourceId);
    }
}
