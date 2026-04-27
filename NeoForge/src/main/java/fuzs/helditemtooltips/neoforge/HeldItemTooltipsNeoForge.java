package fuzs.helditemtooltips.neoforge;

import fuzs.helditemtooltips.common.HeldItemTooltips;
import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import net.neoforged.fml.common.Mod;

@Mod(HeldItemTooltips.MOD_ID)
public class HeldItemTooltipsNeoForge {

    public HeldItemTooltipsNeoForge() {
        ModConstructor.construct(HeldItemTooltips.MOD_ID, HeldItemTooltips::new);
    }
}
