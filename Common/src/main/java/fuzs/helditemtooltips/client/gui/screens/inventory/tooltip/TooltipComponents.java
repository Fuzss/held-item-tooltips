package fuzs.helditemtooltips.client.gui.screens.inventory.tooltip;

import fuzs.helditemtooltips.client.util.ShulkerHoverTextUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Locale;

public final class TooltipComponents {
    public static final TooltipComponent ITEM_NAME = (lines, stack, player, tooltipFlag, style) -> {

        MutableComponent component = Component.empty().append(stack.getHoverName()).withStyle(stack.getRarity().color);
        if (stack.hasCustomHoverName()) {
            component.withStyle(ChatFormatting.ITALIC);
        }

        lines.add(component.withStyle(style::applyTo));

        if (!tooltipFlag.isAdvanced() && !stack.hasCustomHoverName() && stack.is(Items.FILLED_MAP)) {

            Integer integer = MapItem.getMapId(stack);
            if (integer != null) {

                lines.add(Component.literal("#" + integer).withStyle(ChatFormatting.GRAY).withStyle(style::applyTo));
            }
        }
    };
    public static final TooltipComponent ADDITIONAL = (lines, stack, player, tooltipFlag, style) -> {

        if (Block.byItem(stack.getItem()) instanceof ShulkerBoxBlock) {

            ShulkerHoverTextUtil.appendHoverText(lines, stack, -1, false);
        } else {

            stack.getItem().appendHoverText(stack, player == null ? null : player.level(), lines, tooltipFlag);
        }
    };
    public static final TooltipComponent ENCHANTMENTS = (lines, stack, player, tooltipFlag, style) -> {
        if (stack.hasTag()) {
            List<Component> list = Lists.newArrayList();
            ItemStack.appendEnchantmentNames(list, stack.getEnchantmentTags());
            for (Component component : list) {
                lines.add(Component.empty().append(component).withStyle(style.applyTo(component.getStyle())));
            }
        }
    };
    public static final TooltipComponent COLORING = (lines, stack, player, tooltipFlag, style) -> {
        if (stack.hasTag()) {
            if (stack.getTag().contains("display", 10)) {
                CompoundTag compoundtag = stack.getTag().getCompound("display");
                if (compoundtag.contains("color", 99)) {
                    if (tooltipFlag.isAdvanced()) {
                        lines.add(Component.translatable("item.color", String.format(Locale.ROOT, "#%06X", compoundtag.getInt("color"))).withStyle(ChatFormatting.GRAY).withStyle(style::applyTo));
                    } else {
                        lines.add(Component.translatable("item.dyed").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC).withStyle(style::applyTo));
                    }
                }
            }
        }
    };
    public static final TooltipComponent LORE = (lines, stack, player, tooltipFlag, style) -> {
        if (stack.hasTag()) {

            if (stack.getTag().contains("display", 10)) {
                CompoundTag compoundtag = stack.getTag().getCompound("display");

                if (compoundtag.getTagType("Lore") == 9) {
                    ListTag listtag = compoundtag.getList("Lore", 8);

                    for(int i = 0; i < listtag.size(); ++i) {
                        String s = listtag.getString(i);

                        try {
                            MutableComponent component = Component.Serializer.fromJson(s);
                            if (component != null) {
                                Style loreStyle = Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE).withItalic(true);
                                lines.add(ComponentUtils.mergeStyles(component, loreStyle).withStyle(style::applyTo));
                            }
                        } catch (Exception exception) {
                            compoundtag.remove("Lore");
                        }
                    }
                }
            }
        }
    };
    public static final TooltipComponent MODIFIERS = new ModifiersTooltipComponent();
    public static final UpdatingTooltipComponent UNBREAKABLE = (lines, stack, player, tooltipFlag, style) -> {
        if (stack.hasTag()) {
            if (stack.getTag().getBoolean("Unbreakable")) {
                lines.add(Component.translatable("item.unbreakable").withStyle(ChatFormatting.BLUE).withStyle(style::applyTo));
            }
        }
    };
    public static final UpdatingTooltipComponent DURABILITY = (lines, stack, player, tooltipFlag, style) -> {
        if (stack.isDamaged()) {
            lines.add(Component.translatable("item.durability", stack.getMaxDamage() - stack.getDamageValue(), stack.getMaxDamage()).withStyle(ChatFormatting.GRAY).withStyle(style::applyTo));
        }
    };
    public static final TooltipComponent IDENTIFIER = (lines, stack, player, tooltipFlag, style) -> {
        lines.add(Component.literal(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString()).withStyle(ChatFormatting.DARK_GRAY).withStyle(style::applyTo));
    };
    public static final TooltipComponent NBT_COUNT = (lines, stack, player, tooltipFlag, style) -> {
        if (stack.hasTag()) {
            lines.add(Component.translatable("item.nbt_tags", stack.getTag().getAllKeys().size()).withStyle(ChatFormatting.DARK_GRAY).withStyle(style::applyTo));
        }
    };
}
