package fuzs.helditemtooltips.neoforge.client;

import fuzs.helditemtooltips.HeldItemTooltips;
import fuzs.helditemtooltips.client.HeldItemTooltipsClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = HeldItemTooltips.MOD_ID, dist = Dist.CLIENT)
public class HeldItemTooltipsNeoForgeClient {

    public HeldItemTooltipsNeoForgeClient() {
        ClientModConstructor.construct(HeldItemTooltips.MOD_ID, HeldItemTooltipsClient::new);
    }
}
