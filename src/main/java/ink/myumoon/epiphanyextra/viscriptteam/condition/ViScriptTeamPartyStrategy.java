package ink.myumoon.epiphanyextra.viscriptteam.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import java.util.Locale;

public enum ViScriptTeamPartyStrategy {
    MIN("min"), AVERAGE("average"), LEADER("leader"), MAX("max");

    public static final Codec<ViScriptTeamPartyStrategy> CODEC = Codec.STRING.comapFlatMap(
            value -> byId(value).map(DataResult::success)
                    .orElseGet(() -> DataResult.error(() -> "Unknown ViScriptTeam party strategy '" + value
                            + "' (expected min, average, leader or max)")),
            ViScriptTeamPartyStrategy::id);

    private final String id;
    ViScriptTeamPartyStrategy(String id) { this.id = id; }
    public String id() { return id; }

    private static java.util.Optional<ViScriptTeamPartyStrategy> byId(String value) {
        String normalized = value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
        for (ViScriptTeamPartyStrategy strategy : values()) {
            if (strategy.id.equals(normalized)) return java.util.Optional.of(strategy);
        }
        return java.util.Optional.empty();
    }
}
