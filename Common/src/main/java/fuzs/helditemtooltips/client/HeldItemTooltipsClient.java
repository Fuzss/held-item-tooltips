package fuzs.helditemtooltips.client;

import fuzs.helditemtooltips.HeldItemTooltips;
import fuzs.helditemtooltips.client.gui.screens.inventory.tooltip.HoverTextManager;
import fuzs.helditemtooltips.client.gui.screens.inventory.tooltip.TooltipComponents;
import fuzs.helditemtooltips.client.handler.SelectedItemHandler;
import fuzs.helditemtooltips.config.ClientConfig;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.event.v1.ClientTickEvents;
import fuzs.puzzleslib.api.client.event.v1.gui.RenderGuiLayerEvents;

public class HeldItemTooltipsClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerHandlers();
    }

    private static void registerHandlers() {
        ClientTickEvents.END.register(SelectedItemHandler.INSTANCE::onEndClientTick);
        RenderGuiLayerEvents.before(RenderGuiLayerEvents.SELECTED_ITEM_NAME).register(SelectedItemHandler.INSTANCE::onBeforeRenderGuiLayer);
    }

    @Override
    public void onClientSetup() {
        ClientConfig config = HeldItemTooltips.CONFIG.get(ClientConfig.class);
        HoverTextManager.register(TooltipComponents.ITEM_NAME, config.itemName);
        HoverTextManager.register(TooltipComponents.ADDITIONAL, config.additional);
        HoverTextManager.register(TooltipComponents.JUKEBOX_PLAYABLE, config.jukebox);
        HoverTextManager.register(TooltipComponents.TRIM, config.trim);
        HoverTextManager.register(TooltipComponents.STORED_ENCHANTMENTS, config.storedEnchantments);
        HoverTextManager.register(TooltipComponents.ENCHANTMENTS, config.enchantments);
        HoverTextManager.register(TooltipComponents.DYED_COLOR, config.dyeColor);
        HoverTextManager.register(TooltipComponents.LORE, config.lore);
        HoverTextManager.register(TooltipComponents.ATTRIBUTE, config.attribute);
        HoverTextManager.register(TooltipComponents.UNBREAKABLE, config.unbreakable);
        HoverTextManager.register(TooltipComponents.DURABILITY, config.durability);
        HoverTextManager.register(TooltipComponents.IDENTIFIER, config.identifier);
        HoverTextManager.register(TooltipComponents.COMPONENT_COUNT, config.componentCount);
    }
}
