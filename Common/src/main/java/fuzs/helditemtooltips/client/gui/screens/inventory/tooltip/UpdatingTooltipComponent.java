package fuzs.helditemtooltips.client.gui.screens.inventory.tooltip;

@FunctionalInterface
public interface UpdatingTooltipComponent extends TooltipComponent {

    @Override
    default boolean alwaysUpdate() {
        return true;
    }
}
