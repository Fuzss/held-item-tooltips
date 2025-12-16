package fuzs.helditemtooltips.client;

import fuzs.helditemtooltips.HeldItemTooltips;
import fuzs.helditemtooltips.client.gui.screens.inventory.tooltip.HoverTextManager;
import fuzs.helditemtooltips.client.gui.screens.inventory.tooltip.component.TooltipComponents;
import fuzs.helditemtooltips.client.handler.SelectedItemHandler;
import fuzs.helditemtooltips.config.ClientConfig;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.GuiLayersContext;
import fuzs.puzzleslib.api.client.event.v1.ClientTickEvents;

public class HeldItemTooltipsClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        ClientTickEvents.END.register(SelectedItemHandler.INSTANCE::onEndClientTick);
    }

    @Override
    public void onClientSetup() {
        ClientConfig config = HeldItemTooltips.CONFIG.get(ClientConfig.class);
        HoverTextManager.register(TooltipComponents.ITEM_NAME, config.tooltipLines.itemName);
        HoverTextManager.register(TooltipComponents.ADDITIONAL, config.tooltipLines.additional);
        HoverTextManager.register(TooltipComponents.COMPONENTS, config.tooltipLines.components);
        HoverTextManager.register(TooltipComponents.ATTRIBUTE_MODIFIERS, config.tooltipLines.attributeModifiers);
        HoverTextManager.register(TooltipComponents.DISABLED, config.tooltipLines.disabled);
        HoverTextManager.register(TooltipComponents.DURABILITY, config.tooltipLines.durability);
        HoverTextManager.register(TooltipComponents.IDENTIFIER, config.tooltipLines.identifier);
        HoverTextManager.register(TooltipComponents.COMPONENT_COUNT, config.tooltipLines.componentCount);
    }

    @Override
    public void onRegisterGuiLayers(GuiLayersContext context) {
        context.replaceGuiLayer(GuiLayersContext.HELD_ITEM_TOOLTIP, (GuiLayersContext.Layer layer) -> {
            return SelectedItemHandler.INSTANCE::renderSelectedItemName;
        });
    }
}
