package ink.myumoon.epiphanyextra.tide;

import com.li64.tide.data.player.FishStats;
import com.li64.tide.data.player.TidePlayerData;
import com.li64.tide.util.TideUtils;
import ink.myumoon.epiphany.content.condition.Comparison;
import ink.myumoon.epiphanyextra.EpiphanyExtra;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/** The only class in EpiphanyExtra that directly references Tide classes. */
final class TideInternal {
    private static final Set<ResourceLocation> UNKNOWN_FISH = ConcurrentHashMap.newKeySet();
    private static final Set<ResourceLocation> NON_JOURNAL_FISH = ConcurrentHashMap.newKeySet();
    private static final Set<Integer> INVALID_COUNTS = ConcurrentHashMap.newKeySet();

    private TideInternal() {}

    static boolean hasCaught(ServerPlayer player, ResourceLocation fishId,
                             Comparison comparison, int expectedCount) {
        if (expectedCount <= 0) {
            if (INVALID_COUNTS.add(expectedCount)) {
                EpiphanyExtra.LOGGER.warn("Invalid Tide fish catch count {} (must be positive)", expectedCount);
            }
            return false;
        }

        try {
            Registry<Item> items = player.server.registryAccess().registryOrThrow(Registries.ITEM);
            Holder.Reference<Item> holder = items.getHolder(fishId).orElse(null);
            if (holder == null) {
                if (UNKNOWN_FISH.add(fishId)) {
                    EpiphanyExtra.LOGGER.warn("Unknown Tide fish item {}", fishId);
                }
                return false;
            }

            Item item = holder.value();
            if (!TideUtils.isJournalFish(item)) {
                if (NON_JOURNAL_FISH.add(fishId)) {
                    EpiphanyExtra.LOGGER.warn("Item {} is not a Tide journal fish", fishId);
                }
                return false;
            }

            TidePlayerData playerData = TidePlayerData.getOrCreate(player);
            int actualCount = playerData.getDataFor(item)
                    .flatMap(fishData -> fishData.stats)
                    .map(FishStats::getAmountCaught)
                    .orElse(0);
            return comparison.test(actualCount, expectedCount);
        } catch (RuntimeException exception) {
            EpiphanyExtra.LOGGER.warn("Failed to evaluate Tide fish catch record {}", fishId, exception);
            return false;
        }
    }
}
