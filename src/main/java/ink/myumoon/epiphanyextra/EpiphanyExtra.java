package ink.myumoon.epiphanyextra;

import com.mojang.logging.LogUtils;
import ink.myumoon.epiphanyextra.ars.ArsCompat;
import ink.myumoon.epiphanyextra.origin.OriginCompat;
import ink.myumoon.epiphanyextra.oritech.OritechCompat;
import ink.myumoon.epiphanyextra.tide.TideCompat;
import ink.myumoon.epiphanyextra.reskillable.ReskillableCompat;
import ink.myumoon.epiphanyextra.viscriptteam.ViScriptTeamCompat;
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
        OritechCompat.init(modEventBus);
        ArsCompat.init(modEventBus);
        TideCompat.init(modEventBus);
        ReskillableCompat.init(modEventBus);
        ViScriptTeamCompat.init(modEventBus);
        LOGGER.info("Epiphany Extra initialized");
    }
}
