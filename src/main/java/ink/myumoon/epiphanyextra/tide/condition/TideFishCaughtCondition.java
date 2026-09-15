package ink.myumoon.epiphanyextra.tide.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ink.myumoon.epiphany.content.condition.Comparison;
import ink.myumoon.epiphany.content.condition.Condition;
import ink.myumoon.epiphanyextra.tide.TideHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/** Checks a player's persistent Tide journal catch count for one fish item. */
public record TideFishCaughtCondition(ResourceLocation fish, Comparison comparison, int count)
        implements Condition {
    public static final MapCodec<TideFishCaughtCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("fish").forGetter(TideFishCaughtCondition::fish),
            Comparison.CODEC.optionalFieldOf("comparison", Comparison.GREATER_OR_EQUAL)
                    .forGetter(TideFishCaughtCondition::comparison),
            com.mojang.serialization.Codec.INT.optionalFieldOf("count", 1)
                    .forGetter(TideFishCaughtCondition::count)
    ).apply(instance, TideFishCaughtCondition::new));

    @Override
    public MapCodec<? extends Condition> codec() {
        return CODEC;
    }

    @Override
    public boolean test(ServerPlayer player) {
        return TideHelper.hasCaught(player, fish, comparison, count);
    }
}
