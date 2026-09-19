package ink.myumoon.epiphanyextra.viscriptquests.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ink.myumoon.epiphany.content.condition.Condition;
import ink.myumoon.epiphanyextra.viscriptquests.ViScriptQuestsHelper;
import net.minecraft.server.level.ServerPlayer;

/** Checks a ViScriptQuests task/step status. */
public record ViScriptQuestsTaskCondition(String quest, String task, String status) implements Condition {
    public static final MapCodec<ViScriptQuestsTaskCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("quest").forGetter(ViScriptQuestsTaskCondition::quest),
            Codec.STRING.fieldOf("task").forGetter(ViScriptQuestsTaskCondition::task),
            Codec.STRING.fieldOf("status").forGetter(ViScriptQuestsTaskCondition::status)
    ).apply(instance, ViScriptQuestsTaskCondition::new));

    @Override public MapCodec<? extends Condition> codec() { return CODEC; }
    @Override public boolean test(ServerPlayer player) {
        return ViScriptQuestsHelper.hasTask(player, quest, task, status);
    }
}
