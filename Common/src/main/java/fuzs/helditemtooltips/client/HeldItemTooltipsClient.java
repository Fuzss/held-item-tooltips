package fuzs.helditemtooltips.client;

import fuzs.helditemtooltips.HeldItemTooltips;
import fuzs.helditemtooltips.client.gui.screens.inventory.tooltip.HoverTextManager;
import fuzs.helditemtooltips.client.gui.screens.inventory.tooltip.TooltipComponents;
import fuzs.helditemtooltips.client.handler.SelectedItemHandler;
import fuzs.helditemtooltips.config.ClientConfig;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.event.v1.ClientTickEvents;
import fuzs.puzzleslib.api.client.event.v1.renderer.RenderGuiElementEvents;

public class HeldItemTooltipsClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerHandlers();
    }

    private static void registerHandlers() {
        ClientTickEvents.END.register(SelectedItemHandler.INSTANCE::onClientTick$End);
        RenderGuiElementEvents.before(RenderGuiElementEvents.ITEM_NAME).register(SelectedItemHandler.INSTANCE::onRenderGuiOverlay$ItemName);
    }

    @Override
    public void onClientSetup() {
        ClientConfig config = HeldItemTooltips.CONFIG.get(ClientConfig.class);
        HoverTextManager.register(TooltipComponents.ITEM_NAME, config.itemName);
        HoverTextManager.register(TooltipComponents.ADDITIONAL, config.additional);
        HoverTextManager.register(TooltipComponents.ENCHANTMENTS, config.enchantments);
        HoverTextManager.register(TooltipComponents.COLORING, config.coloring);
        HoverTextManager.register(TooltipComponents.LORE, config.lore);
        HoverTextManager.register(TooltipComponents.MODIFIERS, config.modifiers);
        HoverTextManager.register(TooltipComponents.UNBREAKABLE, config.unbreakable);
        HoverTextManager.register(TooltipComponents.DURABILITY, config.durability);
        HoverTextManager.register(TooltipComponents.IDENTIFIER, config.identifier);
        HoverTextManager.register(TooltipComponents.NBT_COUNT, config.nbtCount);
    }
}
