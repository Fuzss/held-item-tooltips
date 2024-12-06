package fuzs.helditemtooltips.client.gui.screens.inventory.tooltip;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.saveddata.maps.MapId;

import java.util.ArrayList;
import java.util.List;

public final class TooltipComponents {
    public static final TooltipComponent ITEM_NAME = (ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> tooltipLines, TooltipFlag tooltipFlag) -> {
        MutableComponent component = Component.empty().append(itemStack.getHoverName()).withStyle(
                itemStack.getRarity().color());
        if (itemStack.has(DataComponents.CUSTOM_NAME)) {
            component.withStyle(ChatFormatting.ITALIC);
        }

        tooltipLines.add(component);
        if (!tooltipFlag.isAdvanced() && !itemStack.has(DataComponents.CUSTOM_NAME) && itemStack.is(Items.FILLED_MAP)) {
            MapId mapId = itemStack.get(DataComponents.MAP_ID);
            if (mapId != null) {
                tooltipLines.add(MapItem.getTooltipForId(mapId));
            }
        }
    };
    public static final TooltipComponent ADDITIONAL = (ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> tooltipLines, TooltipFlag tooltipFlag) -> {
        if (!itemStack.has(DataComponents.HIDE_ADDITIONAL_TOOLTIP)) {
            if (itemStack.has(DataComponents.CONTAINER)) {
                // important for handling shulker boxes separately, so that the last line show the correct amount for cut off lines
                for (ItemStack item : itemStack.get(DataComponents.CONTAINER).nonEmptyItems()) {
                    tooltipLines.add(Component.translatable("container.shulkerBox.itemCount", item.getHoverName(),
                            item.getCount()
                    ).withStyle(ChatFormatting.GRAY));
                }
            } else {
                itemStack.getItem().appendHoverText(itemStack, tooltipContext, tooltipLines, tooltipFlag);
            }
        }
    };
    public static final TooltipComponent JUKEBOX_PLAYABLE = TooltipComponent.fromProvider(
            DataComponents.JUKEBOX_PLAYABLE);
    public static final TooltipComponent TRIM = (ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> tooltipLines, TooltipFlag tooltipFlag) -> {
        List<Component> tmp = new ArrayList<>();
        TooltipComponent.fromProvider(DataComponents.TRIM).addToTooltip(itemStack, tooltipContext, tmp, tooltipFlag);
        if (tmp.size() == 3) {
            // the armor trim type line is enough, it has the color of the material
            tooltipLines.add(tmp.get(1));
        } else {
            tooltipLines.addAll(tmp);
        }
    };
    public static final TooltipComponent STORED_ENCHANTMENTS = TooltipComponent.fromProvider(
            DataComponents.STORED_ENCHANTMENTS);
    public static final TooltipComponent ENCHANTMENTS = TooltipComponent.fromProvider(DataComponents.ENCHANTMENTS);
    public static final TooltipComponent DYED_COLOR = TooltipComponent.fromProvider(DataComponents.DYED_COLOR);
    public static final TooltipComponent LORE = TooltipComponent.fromProvider(DataComponents.LORE);
    public static final TooltipComponent ATTRIBUTE = new AttributeTooltipComponent();
    public static final TooltipComponent UNBREAKABLE = TooltipComponent.fromProvider(DataComponents.UNBREAKABLE);
    public static final TooltipComponent DURABILITY = new TooltipComponent() {

        @Override
        public void addToTooltip(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> tooltipLines, TooltipFlag tooltipFlag) {
            if (itemStack.isDamaged()) {
                tooltipLines.add(
                        Component.translatable("item.durability", itemStack.getMaxDamage() - itemStack.getDamageValue(),
                                itemStack.getMaxDamage()
                        ).withStyle(ChatFormatting.GRAY));
            }
        }

        @Override
        public boolean alwaysUpdate() {
            return true;
        }
    };
    public static final TooltipComponent IDENTIFIER = (ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> tooltipLines, TooltipFlag tooltipFlag) -> {
        tooltipLines.add(Component.literal(BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString())
                .withStyle(ChatFormatting.GRAY));
    };
    public static final TooltipComponent COMPONENT_COUNT = (ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> tooltipLines, TooltipFlag tooltipFlag) -> {
        if (!itemStack.getComponents().isEmpty()) {
            tooltipLines.add(Component.translatable("item.components", itemStack.getComponents().size())
                    .withStyle(ChatFormatting.GRAY));
        }
    };
}
