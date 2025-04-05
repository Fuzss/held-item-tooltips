package fuzs.helditemtooltips.fabric.client.core;

import fuzs.helditemtooltips.client.core.ClientAbstractions;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class FabricClientAbstractions implements ClientAbstractions {

    @Override
    public void onItemTooltip(ItemStack itemStack, List<Component> tooltipLines, Item.TooltipContext tooltipContext, TooltipFlag tooltipType) {
        ItemTooltipCallback.EVENT.invoker().getTooltip(itemStack, tooltipContext, tooltipType, tooltipLines);
    }
}
