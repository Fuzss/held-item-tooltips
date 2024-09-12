package fuzs.helditemtooltips.fabric.client.core;

import fuzs.helditemtooltips.client.core.ClientAbstractions;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class FabricClientAbstractions implements ClientAbstractions {

    @Override
    public void getTooltipLines(ItemStack stack, TooltipFlag context, List<Component> lines) {
        ItemTooltipCallback.EVENT.invoker().getTooltip(stack, context, lines);
    }
}
