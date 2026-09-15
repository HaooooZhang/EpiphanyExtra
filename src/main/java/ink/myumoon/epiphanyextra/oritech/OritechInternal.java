package ink.myumoon.epiphanyextra.oritech;

import ink.myumoon.epiphanyextra.EpiphanyExtra;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import rearth.oritech.block.entity.augmenter.PlayerAugments;
import rearth.oritech.block.entity.augmenter.api.Augment;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/** The only class in EpiphanyExtra that directly references Oritech classes. */
final class OritechInternal {
    private static final Set<ResourceLocation> UNKNOWN_AUGMENTS = ConcurrentHashMap.newKeySet();

    private OritechInternal() {}

    static boolean hasAugment(ServerPlayer player, ResourceLocation augmentId, boolean enabledOnly) {
        Augment augment = PlayerAugments.allAugments.get(augmentId);
        if (augment == null) {
            if (UNKNOWN_AUGMENTS.add(augmentId)) {
                EpiphanyExtra.LOGGER.warn("Unknown Oritech augment {}", augmentId);
            }
            return false;
        }

        try {
            return enabledOnly ? augment.isEnabled(player) : augment.isInstalled(player);
        } catch (RuntimeException exception) {
            EpiphanyExtra.LOGGER.warn("Failed to evaluate Oritech augment {}", augmentId, exception);
            return false;
        }
    }
}
