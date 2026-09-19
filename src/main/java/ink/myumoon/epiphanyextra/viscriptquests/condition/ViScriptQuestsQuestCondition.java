package ink.myumoon.epiphanyextra.viscriptquests.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ink.myumoon.epiphany.content.condition.Condition;
import ink.myumoon.epiphanyextra.viscriptquests.ViScriptQuestsHelper;
import net.minecraft.server.level.ServerPlayer;

/** Checks a player's ViScriptQuests quest status. */
public record ViScriptQuestsQuestCondition(String quest, String status) implements Condition {
    public static final MapCodec<ViScriptQuestsQuestCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("quest").forGetter(ViScriptQuestsQuestCondition::quest),
            Codec.STRING.fieldOf("status").forGetter(ViScriptQuestsQuestCondition::status)
    ).apply(instance, ViScriptQuestsQuestCondition::new));

    @Override public MapCodec<? extends Condition> codec() { return CODEC; }
    @Override public boolean test(ServerPlayer player) {
        return ViScriptQuestsHelper.hasQuest(player, quest, status);
    }
}
