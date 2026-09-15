package ink.myumoon.epiphanyextra.origin.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ink.myumoon.epiphany.content.condition.Condition;
import ink.myumoon.epiphanyextra.origin.OriginHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

/** Checks whether a player has an Origin, in any Layer or a selected Layer. */
public record OriginsOriginCondition(String origin, Optional<ResourceLocation> layer) implements Condition {
    public static final MapCodec<OriginsOriginCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("origin").forGetter(OriginsOriginCondition::origin),
            ResourceLocation.CODEC.optionalFieldOf("layer").forGetter(OriginsOriginCondition::layer)
    ).apply(instance, OriginsOriginCondition::new));

    @Override
    public MapCodec<? extends Condition> codec() {
        return CODEC;
    }

    @Override
    public boolean test(ServerPlayer player) {
        return OriginHelper.hasOrigin(player, origin, layer);
    }

    @Override
    public boolean isEventDriven() {
        return true;
    }
}
