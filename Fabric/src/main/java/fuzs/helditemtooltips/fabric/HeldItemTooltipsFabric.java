package fuzs.helditemtooltips.fabric;

import fuzs.helditemtooltips.HeldItemTooltips;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class HeldItemTooltipsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(HeldItemTooltips.MOD_ID, HeldItemTooltips::new);
    }
}
