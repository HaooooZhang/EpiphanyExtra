package ink.myumoon.epiphanyextra.viscriptteam.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ink.myumoon.epiphany.content.condition.Comparison;
import ink.myumoon.epiphany.content.condition.Condition;
import ink.myumoon.epiphanyextra.viscriptteam.ViScriptTeamHelper;
import net.minecraft.server.level.ServerPlayer;

/** Checks a Party's effective standing toward a faction. */
public record ViScriptTeamPartyStandingCondition(String faction, ViScriptTeamPartyStrategy strategy,
                                                  Comparison comparison, int points) implements Condition {
    public static final MapCodec<ViScriptTeamPartyStandingCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("faction").forGetter(ViScriptTeamPartyStandingCondition::faction),
            ViScriptTeamPartyStrategy.CODEC.optionalFieldOf("strategy", ViScriptTeamPartyStrategy.MIN)
                    .forGetter(ViScriptTeamPartyStandingCondition::strategy),
            Comparison.CODEC.optionalFieldOf("comparison", Comparison.GREATER_OR_EQUAL)
                    .forGetter(ViScriptTeamPartyStandingCondition::comparison),
            Codec.INT.fieldOf("points").forGetter(ViScriptTeamPartyStandingCondition::points)
    ).apply(instance, ViScriptTeamPartyStandingCondition::new));

    @Override public MapCodec<? extends Condition> codec() { return CODEC; }
    @Override public boolean test(ServerPlayer player) {
        return ViScriptTeamHelper.hasPartyStanding(player, faction, strategy.id(), comparison, points);
    }
}
