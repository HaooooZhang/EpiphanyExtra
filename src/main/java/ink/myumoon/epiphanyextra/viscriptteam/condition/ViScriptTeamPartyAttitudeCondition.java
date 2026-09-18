package ink.myumoon.epiphanyextra.viscriptteam.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ink.myumoon.epiphany.content.condition.Condition;
import ink.myumoon.epiphanyextra.viscriptteam.ViScriptTeamHelper;
import net.minecraft.server.level.ServerPlayer;

/** Checks a Party's effective attitude toward a faction. */
public record ViScriptTeamPartyAttitudeCondition(String faction, ViScriptTeamPartyStrategy strategy,
                                                  ViScriptTeamAttitude attitude) implements Condition {
    public static final MapCodec<ViScriptTeamPartyAttitudeCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("faction").forGetter(ViScriptTeamPartyAttitudeCondition::faction),
            ViScriptTeamPartyStrategy.CODEC.optionalFieldOf("strategy", ViScriptTeamPartyStrategy.MIN)
                    .forGetter(ViScriptTeamPartyAttitudeCondition::strategy),
            ViScriptTeamAttitude.CODEC.fieldOf("attitude").forGetter(ViScriptTeamPartyAttitudeCondition::attitude)
    ).apply(instance, ViScriptTeamPartyAttitudeCondition::new));

    @Override public MapCodec<? extends Condition> codec() { return CODEC; }
    @Override public boolean test(ServerPlayer player) {
        return ViScriptTeamHelper.hasPartyAttitude(player, faction, strategy.id(), attitude.id());
    }
}
