package ink.myumoon.epiphanyextra.viscriptquests.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ink.myumoon.epiphany.content.condition.Comparison;
import ink.myumoon.epiphany.content.condition.Condition;
import ink.myumoon.epiphanyextra.viscriptquests.ViScriptQuestsHelper;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

/** Checks a ViScriptQuests objective status and/or current amount. */
public record ViScriptQuestsObjectiveCondition(String quest, String task, String objective,
                                               Optional<String> status, Optional<Comparison> comparison,
                                               Optional<Integer> amount) implements Condition {
    private static final Codec<Integer> NON_NEGATIVE_INT = Codec.INT.flatXmap(
            value -> value >= 0 ? DataResult.success(value)
                    : DataResult.error(() -> "ViScriptQuests objective amount cannot be negative"),
            value -> value >= 0 ? DataResult.success(value)
                    : DataResult.error(() -> "ViScriptQuests objective amount cannot be negative"));

    public static final MapCodec<ViScriptQuestsObjectiveCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("quest").forGetter(ViScriptQuestsObjectiveCondition::quest),
            Codec.STRING.fieldOf("task").forGetter(ViScriptQuestsObjectiveCondition::task),
            Codec.STRING.fieldOf("objective").forGetter(ViScriptQuestsObjectiveCondition::objective),
            Codec.STRING.optionalFieldOf("status").forGetter(ViScriptQuestsObjectiveCondition::status),
            Comparison.CODEC.optionalFieldOf("comparison").forGetter(ViScriptQuestsObjectiveCondition::comparison),
            NON_NEGATIVE_INT.optionalFieldOf("amount").forGetter(ViScriptQuestsObjectiveCondition::amount)
    ).apply(instance, ViScriptQuestsObjectiveCondition::new));

    @Override public MapCodec<? extends Condition> codec() { return CODEC; }
    @Override public boolean test(ServerPlayer player) {
        return ViScriptQuestsHelper.hasObjective(player, quest, task, objective, status, comparison, amount);
    }
}
