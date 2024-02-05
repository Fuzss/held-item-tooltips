package fuzs.helditemtooltips.client.gui.screens.inventory.tooltip;

import fuzs.helditemtooltips.HeldItemTooltips;
import fuzs.helditemtooltips.client.core.ClientAbstractions;
import fuzs.helditemtooltips.config.ClientConfig;
import fuzs.helditemtooltips.config.TooltipComponentConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HoverTextManager {
    private static final List<TooltipComponentHolder> TOOLTIP_COMPONENT_HOLDERS = Lists.newArrayList();

    public static void register(TooltipComponent tooltipComponent, TooltipComponentConfig settings) {

        TOOLTIP_COMPONENT_HOLDERS.add(new TooltipComponentHolder(tooltipComponent, settings));
    }

    public static List<Component> getTooltipLines(ItemStack stack, @Nullable Player player, int maxLines) {

        List<TooltipComponentHolder> holders = TOOLTIP_COMPONENT_HOLDERS.stream().filter(TooltipComponentHolder::include).collect(Collectors.toList());
        holders.forEach(holder -> holder.tryRebuild(stack, player));
        boolean includeLastLine = HeldItemTooltips.CONFIG.get(ClientConfig.class).lastLine && maxLines > 1;
        if (holders.stream().mapToInt(TooltipComponentHolder::size).sum() > maxLines && includeLastLine) {
            maxLines--;
        }

        holders.sort(Comparator.comparingInt(TooltipComponentHolder::priority).reversed());
        for (TooltipComponentHolder holder : holders) {
            maxLines = holder.subtractLines(maxLines);
        }

        holders.sort(Comparator.comparingInt(TooltipComponentHolder::ordering));
        List<Component> lines = holders.stream()
                .map(TooltipComponentHolder::getLines)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        maxLines = applyAdditionalInformation(lines, stack, maxLines);

        if (includeLastLine && maxLines < 0) {
            lines.add(Component.translatable("container.shulkerBox.more", (-1) * maxLines).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }

        return lines;
    }

    public static void reset() {

        TOOLTIP_COMPONENT_HOLDERS.forEach(TooltipComponentHolder::clear);
    }

    private static int applyAdditionalInformation(List<Component> lines, ItemStack stack, int maxLines) {

        if (!HeldItemTooltips.CONFIG.get(ClientConfig.class).additionalInformation) return maxLines;

        int oldSize = lines.size();
        ClientAbstractions.INSTANCE.getTooltipLines(stack, TooltipFlag.Default.NORMAL, lines);
        if (lines.size() - oldSize > Math.max(maxLines, 0)) {

            lines.subList(oldSize + Math.max(maxLines, 0), lines.size()).clear();
        }

        return maxLines - (lines.size() - oldSize);
    }
}
