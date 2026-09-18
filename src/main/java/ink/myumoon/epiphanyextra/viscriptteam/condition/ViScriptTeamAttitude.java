package ink.myumoon.epiphanyextra.viscriptteam.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import java.util.Locale;

public enum ViScriptTeamAttitude {
    HOSTILE("hostile"), NEUTRAL("neutral"), FRIENDLY("friendly");

    public static final Codec<ViScriptTeamAttitude> CODEC = Codec.STRING.comapFlatMap(
            value -> byId(value).map(DataResult::success)
                    .orElseGet(() -> DataResult.error(() -> "Unknown ViScriptTeam attitude '" + value
                            + "' (expected hostile, neutral or friendly)")),
            ViScriptTeamAttitude::id);

    private final String id;
    ViScriptTeamAttitude(String id) { this.id = id; }
    public String id() { return id; }

    private static java.util.Optional<ViScriptTeamAttitude> byId(String value) {
        String normalized = value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
        for (ViScriptTeamAttitude attitude : values()) {
            if (attitude.id.equals(normalized)) return java.util.Optional.of(attitude);
        }
        return java.util.Optional.empty();
    }
}
