package ink.myumoon.epiphanyextra.fieldguide;

import com.evandev.fieldguide.server.ServerFieldGuideManager;
import com.evandev.fieldguide.server.progress.FieldGuideProgressManager;
import com.evandev.fieldguide.server.progress.PlayerFieldGuideProgress;
import ink.myumoon.epiphanyextra.EpiphanyExtra;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/** The only class in EpiphanyExtra that directly references Field Guide classes. */
final class FieldGuideInternal {
    private static final Set<ResourceLocation> UNKNOWN_ENTITIES = ConcurrentHashMap.newKeySet();
    private static final Set<ResourceLocation> MISSING_ENTRIES = ConcurrentHashMap.newKeySet();
    private static final Set<String> DIAGNOSTICS = ConcurrentHashMap.newKeySet();

    private FieldGuideInternal() {}

    static boolean isEntityUnlocked(ServerPlayer player, ResourceLocation entityId) {
        try {
            Registry<EntityType<?>> entities = BuiltInRegistries.ENTITY_TYPE;
            if (!entities.containsKey(entityId)) {
                if (UNKNOWN_ENTITIES.add(entityId)) {
                    EpiphanyExtra.LOGGER.warn("Unknown Field Guide entity type {}", entityId);
                }
                return false;
            }

            ServerFieldGuideManager guide = ServerFieldGuideManager.getInstance();
            if (!guide.hasEntry(entityId)) {
                if (MISSING_ENTRIES.add(entityId)) {
                    EpiphanyExtra.LOGGER.warn("Field Guide has no entity entry for {}", entityId);
                }
                return false;
            }

            PlayerFieldGuideProgress progress = FieldGuideProgressManager.getInstance().getProgress(player);
            if (progress == null) {
                if (DIAGNOSTICS.add("progress")) {
                    EpiphanyExtra.LOGGER.warn("Field Guide player progress is unavailable");
                }
                return false;
            }
            return progress.isUnlocked(entityId);
        } catch (RuntimeException exception) {
            EpiphanyExtra.LOGGER.warn("Failed to evaluate Field Guide entity {}", entityId, exception);
            return false;
        }
    }
}
