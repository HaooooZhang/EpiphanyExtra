package ink.myumoon.epiphanyextra.oritech.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import ink.myumoon.epiphany.content.condition.Condition;
import ink.myumoon.epiphanyextra.oritech.OritechHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * Checks whether a player has an Oritech augment installed or enabled.
 *
 * JSON: {@code {"type":"epiphany_extra:oritech_augment",
 * "augment":"oritech:augment/flight","state":"installed"}}
 */
public record OritechAugmentCondition(ResourceLocation augment, State state) implements Condition {
    public static final MapCodec<OritechAugmentCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("augment").forGetter(OritechAugmentCondition::augment),
            State.CODEC.optionalFieldOf("state", State.INSTALLED).forGetter(OritechAugmentCondition::state)
    ).apply(instance, OritechAugmentCondition::new));

    @Override
    public MapCodec<? extends Condition> codec() {
        return CODEC;
    }

    @Override
    public boolean test(ServerPlayer player) {
        return OritechHelper.hasAugment(player, augment, state == State.ENABLED);
    }

    public enum State {
        INSTALLED("installed"),
        ENABLED("enabled");

        public static final Codec<State> CODEC = Codec.STRING.comapFlatMap(
                value -> switch (value) {
                    case "installed" -> DataResult.success(INSTALLED);
                    case "enabled" -> DataResult.success(ENABLED);
                    default -> DataResult.error(() -> "Unknown Oritech augment state '" + value
                            + "' (expected installed or enabled)");
                },
                State::serializedName
        );

        private final String serializedName;

        State(String serializedName) {
            this.serializedName = serializedName;
        }

        private String serializedName() {
            return serializedName;
        }
    }
}
