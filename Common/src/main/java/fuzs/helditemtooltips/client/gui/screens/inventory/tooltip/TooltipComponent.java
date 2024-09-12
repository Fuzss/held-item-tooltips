package fuzs.helditemtooltips.client.gui.screens.inventory.tooltip;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@FunctionalInterface
public interface TooltipComponent {

    void appendTooltipLines(List<Component> lines, ItemStack stack, @Nullable Player player, TooltipFlag tooltipFlag, Style style);

    default boolean alwaysUpdate() {
        return false;
    }
}
