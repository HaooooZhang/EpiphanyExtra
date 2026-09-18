package ink.myumoon.epiphanyextra.viscriptteam.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ink.myumoon.epiphany.content.condition.Comparison;
import ink.myumoon.epiphany.content.condition.Condition;
import ink.myumoon.epiphanyextra.viscriptteam.ViScriptTeamHelper;
import net.minecraft.server.level.ServerPlayer;

/** Checks a player's personal standing with a ViScriptTeam faction. */
public record ViScriptTeamStandingCondition(String faction, Comparison comparison, int points)
        implements Condition {
    public static final MapCodec<ViScriptTeamStandingCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("faction").forGetter(ViScriptTeamStandingCondition::faction),
            Comparison.CODEC.optionalFieldOf("comparison", Comparison.GREATER_OR_EQUAL)
                    .forGetter(ViScriptTeamStandingCondition::comparison),
            Codec.INT.fieldOf("points").forGetter(ViScriptTeamStandingCondition::points)
    ).apply(instance, ViScriptTeamStandingCondition::new));

    @Override public MapCodec<? extends Condition> codec() { return CODEC; }
    @Override public boolean test(ServerPlayer player) {
        return ViScriptTeamHelper.hasStanding(player, faction, comparison, points);
    }
}
