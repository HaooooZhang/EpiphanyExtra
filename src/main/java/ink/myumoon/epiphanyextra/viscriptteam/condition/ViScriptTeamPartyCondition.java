package ink.myumoon.epiphanyextra.viscriptteam.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ink.myumoon.epiphany.content.condition.Condition;
import ink.myumoon.epiphanyextra.viscriptteam.ViScriptTeamHelper;
import net.minecraft.server.level.ServerPlayer;

/** Checks whether a player belongs to a named ViScriptTeam Party. */
public record ViScriptTeamPartyCondition(String party) implements Condition {
    public static final MapCodec<ViScriptTeamPartyCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("party").forGetter(ViScriptTeamPartyCondition::party)
    ).apply(instance, ViScriptTeamPartyCondition::new));

    @Override public MapCodec<? extends Condition> codec() { return CODEC; }
    @Override public boolean test(ServerPlayer player) { return ViScriptTeamHelper.hasParty(player, party); }
}
