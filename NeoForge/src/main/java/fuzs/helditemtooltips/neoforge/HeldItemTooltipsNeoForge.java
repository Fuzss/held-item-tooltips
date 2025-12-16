package fuzs.helditemtooltips.neoforge;

import fuzs.helditemtooltips.HeldItemTooltips;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.neoforged.fml.common.Mod;

@Mod(HeldItemTooltips.MOD_ID)
public class HeldItemTooltipsNeoForge {

    public HeldItemTooltipsNeoForge() {
        ModConstructor.construct(HeldItemTooltips.MOD_ID, HeldItemTooltips::new);
    }
}
