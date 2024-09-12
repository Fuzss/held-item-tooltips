package fuzs.helditemtooltips.client.gui.screens.inventory.tooltip;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import fuzs.helditemtooltips.config.TooltipComponentConfig;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TooltipComponentHolder {
    private static final Map<TooltipComponent, ItemStack.TooltipPart> TOOLTIP_COMPONENT_TO_PART = ImmutableMap.<TooltipComponent, ItemStack.TooltipPart>builder()
            .put(TooltipComponents.ENCHANTMENTS, ItemStack.TooltipPart.ENCHANTMENTS)
            .put(TooltipComponents.MODIFIERS, ItemStack.TooltipPart.MODIFIERS)
            .put(TooltipComponents.UNBREAKABLE, ItemStack.TooltipPart.UNBREAKABLE)
            .put(TooltipComponents.ADDITIONAL, ItemStack.TooltipPart.ADDITIONAL)
            .put(TooltipComponents.COLORING, ItemStack.TooltipPart.DYE)
            .build();

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

    public void tryRebuild(ItemStack stack, @Nullable Player player) {
        if (this.lines == null || this.component.alwaysUpdate()) {
            if (!this.settings.respectHideFlags || shouldShowInTooltip(stack, this.component)) {
                // initialize list with empty component as some mods expect the title to be present when performing index based operations on the list
                // looking at you https://github.com/Noaaan/MythicMetals
                List<Component> lines = Lists.newArrayList(CommonComponents.EMPTY);
                TooltipFlag.Default tooltipFlag = this.settings.advancedTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL;
                Style style = this.settings.getStyle();
                this.component.appendTooltipLines(lines, stack, player, tooltipFlag, style);
                lines.removeIf(component -> component.getString().isEmpty());
                this.lines = Collections.unmodifiableList(lines);
            } else {
                this.lines = Collections.emptyList();
            }
        }
    }

    public int subtractLines(int maxLines) {
        this.maxLines = Math.min(this.size(), Math.max(maxLines, 0));
        return maxLines - this.size();
    }

    public List<Component> getLines() {
        Objects.requireNonNull(this.lines, "lines is null");
        return this.lines.subList(0, this.maxLines);
    }

    public int size() {
        Objects.requireNonNull(this.lines, "lines is null");
        return this.lines.size();
    }

    private static boolean shouldShowInTooltip(ItemStack stack, TooltipComponent component) {
        if (!TOOLTIP_COMPONENT_TO_PART.containsKey(component)) return true;
        int hideFlags = stack.hasTag() && stack.getTag().contains("HideFlags", 99) ? stack.getTag().getInt("HideFlags") : 0;
        return (hideFlags & TOOLTIP_COMPONENT_TO_PART.get(component).getMask()) == 0;
    }
}
