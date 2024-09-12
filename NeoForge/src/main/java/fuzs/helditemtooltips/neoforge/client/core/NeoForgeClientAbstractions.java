package fuzs.helditemtooltips.neoforge.client.core;

import fuzs.helditemtooltips.client.core.ClientAbstractions;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.event.EventHooks;

import java.util.List;

public class NeoForgeClientAbstractions implements ClientAbstractions {

    @Override
    public void getTooltipLines(ItemStack stack, TooltipFlag context, List<Component> lines) {
        Minecraft minecraft = Minecraft.getInstance();
        EventHooks.onItemTooltip(stack, minecraft.player, lines, context);
    }
}
