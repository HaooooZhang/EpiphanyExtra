package ink.myumoon.epiphanyextra.reskillable.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ink.myumoon.epiphany.content.condition.Comparison;
import ink.myumoon.epiphany.content.condition.Condition;
import ink.myumoon.epiphanyextra.reskillable.ReskillableHelper;
import net.minecraft.server.level.ServerPlayer;

/** Checks a Reskillable skill's current level. */
public record ReskillableSkillLevelCondition(String skill, Comparison comparison, int level)
        implements Condition {
    public static final MapCodec<ReskillableSkillLevelCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("skill").forGetter(ReskillableSkillLevelCondition::skill),
            Comparison.CODEC.optionalFieldOf("comparison", Comparison.GREATER_OR_EQUAL)
                    .forGetter(ReskillableSkillLevelCondition::comparison),
            com.mojang.serialization.Codec.INT.fieldOf("level").forGetter(ReskillableSkillLevelCondition::level)
    ).apply(instance, ReskillableSkillLevelCondition::new));

    @Override
    public MapCodec<? extends Condition> codec() {
        return CODEC;
    }

    @Override
    public boolean test(ServerPlayer player) {
        return ReskillableHelper.hasSkillLevel(player, skill, comparison, level);
    }
}
