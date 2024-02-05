package fuzs.helditemtooltips.fabric.client;

import fuzs.helditemtooltips.HeldItemTooltips;
import fuzs.helditemtooltips.client.HeldItemTooltipsClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class HeldItemTooltipsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(HeldItemTooltips.MOD_ID, HeldItemTooltipsClient::new);
    }
}
