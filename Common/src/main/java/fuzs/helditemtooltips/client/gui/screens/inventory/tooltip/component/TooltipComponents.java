package fuzs.helditemtooltips.client.gui.screens.inventory.tooltip.component;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public final class TooltipComponents {
    private static final Component DISABLED_ITEM_TOOLTIP = Component.translatable("item.disabled")
            .withStyle(ChatFormatting.RED);
    public static final TooltipComponent ITEM_NAME = (ItemStack itemStack, Item.TooltipContext tooltipContext, Consumer<Component> componentConsumer, TooltipFlag tooltipFlag) -> {
        componentConsumer.accept(itemStack.getStyledHoverName());
    };
    public static final TooltipComponent ADDITIONAL = (ItemStack itemStack, Item.TooltipContext tooltipContext, Consumer<Component> componentConsumer, TooltipFlag tooltipFlag) -> {
        TooltipDisplay tooltipDisplay = itemStack.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT);
        itemStack.getItem().appendHoverText(itemStack, tooltipContext, tooltipDisplay, componentConsumer, tooltipFlag);
    };
    public static final TooltipComponent COMPONENTS = new ComponentsTooltipComponent();
    public static final TooltipComponent ATTRIBUTE_MODIFIERS = new AttributeModifiersTooltipComponent();
    public static final TooltipComponent DURABILITY = new TooltipComponent() {
        @Override
        public void addToTooltip(ItemStack itemStack, Item.TooltipContext tooltipContext, Consumer<Component> componentConsumer, TooltipFlag tooltipFlag) {
            if (itemStack.isDamaged()) {
                componentConsumer.accept(Component.translatable("item.durability",
                        itemStack.getMaxDamage() - itemStack.getDamageValue(),
                        itemStack.getMaxDamage()).withStyle(ChatFormatting.GRAY));
            }
        }

        @Override
        public boolean alwaysUpdate() {
            return true;
        }
    };
    public static final TooltipComponent IDENTIFIER = (ItemStack itemStack, Item.TooltipContext tooltipContext, Consumer<Component> componentConsumer, TooltipFlag tooltipFlag) -> {
        componentConsumer.accept(Component.literal(BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString())
                .withStyle(ChatFormatting.GRAY));
    };
    public static final TooltipComponent COMPONENT_COUNT = (ItemStack itemStack, Item.TooltipContext tooltipContext, Consumer<Component> componentConsumer, TooltipFlag tooltipFlag) -> {
        if (!itemStack.getComponents().isEmpty()) {
            componentConsumer.accept(Component.translatable("item.components", itemStack.getComponents().size())
                    .withStyle(ChatFormatting.GRAY));
        }
    };
    public static final TooltipComponent DISABLED = (ItemStack itemStack, Item.TooltipContext tooltipContext, Consumer<Component> componentConsumer, TooltipFlag tooltipFlag) -> {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && !itemStack.getItem().isEnabled(player.level().enabledFeatures())) {
            componentConsumer.accept(DISABLED_ITEM_TOOLTIP);
        }
    };

    private TooltipComponents() {
        // NO-OP
    }
}
