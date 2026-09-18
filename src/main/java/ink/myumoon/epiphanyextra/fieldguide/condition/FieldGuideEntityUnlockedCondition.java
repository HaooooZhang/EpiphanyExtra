package ink.myumoon.epiphanyextra.fieldguide.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ink.myumoon.epiphany.content.condition.Condition;
import ink.myumoon.epiphanyextra.fieldguide.FieldGuideHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/** Checks whether Field Guide has unlocked the entry for one Minecraft entity type. */
public record FieldGuideEntityUnlockedCondition(ResourceLocation entity) implements Condition {
    public static final MapCodec<FieldGuideEntityUnlockedCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("entity").forGetter(FieldGuideEntityUnlockedCondition::entity)
    ).apply(instance, FieldGuideEntityUnlockedCondition::new));

    @Override
    public MapCodec<? extends Condition> codec() {
        return CODEC;
    }

    @Override
    public boolean test(ServerPlayer player) {
        return FieldGuideHelper.isEntityUnlocked(player, entity);
    }
}
