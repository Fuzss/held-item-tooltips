package fuzs.helditemtooltips.neoforge.services;

import fuzs.helditemtooltips.common.services.ClientAbstractions;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.event.EventHooks;

import java.util.List;

public class NeoForgeClientAbstractions implements ClientAbstractions {

    @Override
    public void onItemTooltip(ItemStack itemStack, List<Component> tooltipLines, Item.TooltipContext tooltipContext, TooltipFlag tooltipType) {
        EventHooks.onItemTooltip(itemStack, Minecraft.getInstance().player, tooltipLines, tooltipType, tooltipContext);
    }
}
