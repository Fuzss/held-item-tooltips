package fuzs.helditemtooltips.client.core;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.List;

public class ForgeClientAbstractions implements ClientAbstractions {

    @Override
    public void getTooltipLines(ItemStack stack, TooltipFlag context, List<Component> lines) {
        Minecraft minecraft = Minecraft.getInstance();
        ForgeEventFactory.onItemTooltip(stack, minecraft.player, lines, context);
    }
}
