package fuzs.helditemtooltips.client.gui.screens.inventory.tooltip;

import fuzs.helditemtooltips.HeldItemTooltips;
import fuzs.helditemtooltips.client.core.ClientAbstractions;
import fuzs.helditemtooltips.client.gui.screens.inventory.tooltip.component.TooltipComponent;
import fuzs.helditemtooltips.config.ClientConfig;
import fuzs.helditemtooltips.config.TooltipComponentConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HoverTextManager {
    private static final List<TooltipComponentHolder> TOOLTIP_COMPONENT_HOLDERS = new ArrayList<>();

    public static void register(TooltipComponent tooltipComponent, TooltipComponentConfig settings) {

        TOOLTIP_COMPONENT_HOLDERS.add(new TooltipComponentHolder(tooltipComponent, settings));
    }

    public static List<Component> getTooltipLines(ItemStack itemStack, @Nullable Level level, int maxLines) {

        List<TooltipComponentHolder> holders = TOOLTIP_COMPONENT_HOLDERS.stream()
                .filter(TooltipComponentHolder::include)
                .collect(Collectors.toList());
        Item.TooltipContext tooltipContext = Item.TooltipContext.of(level);
        holders.forEach(holder -> holder.rebuildIfNecessary(itemStack, tooltipContext));
        boolean includeLastLine = HeldItemTooltips.CONFIG.get(ClientConfig.class).lastLine && maxLines > 1;
        if (holders.stream().mapToInt(TooltipComponentHolder::size).sum() > maxLines && includeLastLine) {
            maxLines--;
        }

        holders.sort(Comparator.comparingInt(TooltipComponentHolder::priority).reversed());
        for (TooltipComponentHolder holder : holders) {
            maxLines = holder.subtractLines(maxLines);
        }

        holders.sort(Comparator.comparingInt(TooltipComponentHolder::ordering));
        List<Component> tooltipLines = holders.stream()
                .map(TooltipComponentHolder::getLines)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        maxLines = getAdditionalTooltipLines(itemStack, tooltipLines, tooltipContext, maxLines);

        if (includeLastLine && maxLines < 0) {
            tooltipLines.add(Component.translatable("item.container.more_items", -maxLines)
                    .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }

        return tooltipLines;
    }

    public static void reset() {

        TOOLTIP_COMPONENT_HOLDERS.forEach(TooltipComponentHolder::clear);
    }

    private static int getAdditionalTooltipLines(ItemStack itemStack, List<Component> tooltipLines, Item.TooltipContext tooltipContext, int maxLines) {

        if (!HeldItemTooltips.CONFIG.get(ClientConfig.class).additionalTooltipLines) return maxLines;

        int oldSize = tooltipLines.size();
        ClientAbstractions.INSTANCE.onItemTooltip(itemStack, tooltipLines, tooltipContext, TooltipFlag.Default.NORMAL);
        if (tooltipLines.size() - oldSize > Math.max(maxLines, 0)) {

            tooltipLines.subList(oldSize + Math.max(maxLines, 0), tooltipLines.size()).clear();
        }

        return maxLines - (tooltipLines.size() - oldSize);
    }
}
