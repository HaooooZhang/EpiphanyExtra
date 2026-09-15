package ink.myumoon.epiphanyextra;

import com.mojang.logging.LogUtils;
import ink.myumoon.epiphanyextra.ars.ArsCompat;
import ink.myumoon.epiphanyextra.origin.OriginCompat;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

/** EpiphanyExtra compatibility addon entry point. */
@Mod(EpiphanyExtra.MODID)
public final class EpiphanyExtra {
    public static final String MODID = "epiphany_extra";
    public static final Logger LOGGER = LogUtils.getLogger();

    public EpiphanyExtra(IEventBus modEventBus) {
        OriginCompat.init(modEventBus);
        ArsCompat.init();
        LOGGER.info("Epiphany Extra initialized");
    }
}
