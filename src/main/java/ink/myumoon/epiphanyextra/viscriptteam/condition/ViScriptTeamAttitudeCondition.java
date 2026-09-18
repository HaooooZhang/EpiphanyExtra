package ink.myumoon.epiphanyextra.viscriptteam.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ink.myumoon.epiphany.content.condition.Condition;
import ink.myumoon.epiphanyextra.viscriptteam.ViScriptTeamHelper;
import net.minecraft.server.level.ServerPlayer;

/** Checks a player's personal attitude toward a ViScriptTeam faction. */
public record ViScriptTeamAttitudeCondition(String faction, ViScriptTeamAttitude attitude)
        implements Condition {
    public static final MapCodec<ViScriptTeamAttitudeCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("faction").forGetter(ViScriptTeamAttitudeCondition::faction),
            ViScriptTeamAttitude.CODEC.fieldOf("attitude").forGetter(ViScriptTeamAttitudeCondition::attitude)
    ).apply(instance, ViScriptTeamAttitudeCondition::new));

    @Override public MapCodec<? extends Condition> codec() { return CODEC; }
    @Override public boolean test(ServerPlayer player) {
        return ViScriptTeamHelper.hasAttitude(player, faction, attitude.id());
    }
}
