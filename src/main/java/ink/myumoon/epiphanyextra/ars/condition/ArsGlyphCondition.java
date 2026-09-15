package ink.myumoon.epiphanyextra.ars.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ink.myumoon.epiphany.content.condition.Condition;
import ink.myumoon.epiphanyextra.ars.ArsHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * Checks whether a player has learned an Ars Nouveau Glyph.
 *
 * JSON: {@code {"type":"epiphany_extra:ars_glyph",
 * "glyph":"ars_nouveau:glyph_harm"}}
 */
public record ArsGlyphCondition(ResourceLocation glyph) implements Condition {
    public static final MapCodec<ArsGlyphCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("glyph").forGetter(ArsGlyphCondition::glyph)
    ).apply(instance, ArsGlyphCondition::new));

    @Override
    public MapCodec<? extends Condition> codec() {
        return CODEC;
    }

    @Override
    public boolean test(ServerPlayer player) {
        return ArsHelper.knowsGlyph(player, glyph);
    }
}
