package fuzs.helditemtooltips.client.gui.screens.inventory.tooltip;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.List;

@FunctionalInterface
public interface TooltipComponent {

    void addToTooltip(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> tooltipLines, TooltipFlag tooltipFlag);

    default boolean alwaysUpdate() {
        return false;
    }

    static <T extends TooltipProvider> TooltipComponent fromProvider(DataComponentType<T> dataComponentType) {
        return (ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> tooltipLines, TooltipFlag tooltipFlag) -> {
            T tooltipProvider = itemStack.get(dataComponentType);
            if (tooltipProvider != null) {
                tooltipProvider.addToTooltip(tooltipContext, tooltipLines::add, tooltipFlag);
            }
        };
    }
}
