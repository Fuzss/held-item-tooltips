package fuzs.helditemtooltips.common.client.gui.screens.inventory.tooltip;

import fuzs.helditemtooltips.common.client.gui.screens.inventory.tooltip.component.TooltipComponent;
import fuzs.helditemtooltips.common.config.TooltipComponentConfig;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public final class TooltipComponentHolder {
    private final TooltipComponent component;
    private final TooltipComponentConfig settings;
    private List<Component> lines;
    private int maxLines = -1;

    public TooltipComponentHolder(TooltipComponent component, TooltipComponentConfig settings) {
        this.component = component;
        this.settings = settings;
    }

    public boolean include() {
        return this.settings.include;
    }

    public int ordering() {
        return this.settings.ordering;
    }

    public int priority() {
        return this.settings.priority;
    }

    public void clear() {
        this.lines = null;
    }

    public void rebuildIfNecessary(ItemStack itemStack, Item.TooltipContext tooltipContext) {
        if (this.lines == null || this.component.alwaysUpdate()) {
            // Initialize the list with an empty component as some mods expect the title to be present when performing index-based operations on the list.
            // Looking at you: https://github.com/Noaaan/MythicMetals
            List<Component> tooltipLines = new ArrayList<>(List.of(CommonComponents.EMPTY));
            this.component.addToTooltip(itemStack, tooltipContext, tooltipLines::add, this.settings.tooltipFlag());
            ListIterator<Component> iterator = tooltipLines.listIterator();
            // Remove all empty components and remove spaces at the beginning of components, such as armor trims.
            while (iterator.hasNext()) {
                Component component = iterator.next();
                if (component.getContents() instanceof PlainTextContents contents) {
                    String text = contents.text();
                    if (text.isBlank()) {
                        if (!component.getSiblings().isEmpty()) {
                            if (!text.isEmpty()) {
                                MutableComponent mutableComponent = Component.empty().setStyle(component.getStyle());
                                component.getSiblings().forEach(mutableComponent::append);
                                iterator.set(mutableComponent);
                            }
                        } else {
                            iterator.remove();
                        }
                    }
                }
            }

            Style style = this.settings.style();
            if (!style.isEmpty()) {
                tooltipLines.replaceAll((Component component) -> {
                    return component.copy().withStyle(style::applyTo);
                });
            }

            this.lines = Collections.unmodifiableList(tooltipLines);
        }
    }

    public int subtractLines(int maxLines) {
        this.maxLines = Math.clamp(maxLines, 0, this.size());
        return maxLines - this.size();
    }

    public List<Component> getLines() {
        return this.getLinesOrThrow().subList(0, this.maxLines);
    }

    public int size() {
        return this.getLinesOrThrow().size();
    }

    private List<Component> getLinesOrThrow() {
        Objects.requireNonNull(this.lines, "lines is null");
        return this.lines;
    }
}
