package fuzs.helditemtooltips.client.gui.screens.inventory.tooltip.component;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public final class AttributeModifiersTooltipComponent implements TooltipComponent {

    @Override
    public void addToTooltip(ItemStack itemStack, Item.TooltipContext tooltipContext, Consumer<Component> componentConsumer, TooltipFlag tooltipFlag) {
        // collects attribute modifiers in a condensed format: equipment group lines are skipped, the color format is simplified (only red & blue)
        this.addAttributeTooltips(itemStack, componentConsumer, Minecraft.getInstance().player);
    }

    /**
     * @see ItemStack#addAttributeTooltips(Consumer, TooltipDisplay, Player)
     */
    private void addAttributeTooltips(ItemStack itemStack, Consumer<Component> tooltipAdder, @Nullable Player player) {
        for (EquipmentSlotGroup equipmentSlotGroup : EquipmentSlotGroup.values()) {
            itemStack.forEachModifier(equipmentSlotGroup,
                    (Holder<Attribute> holder, AttributeModifier attributeModifier, ItemAttributeModifiers.Display display) -> {
                        if (display == ItemAttributeModifiers.Display.attributeModifiers()) {
                            Legacy.INSTANCE.apply(tooltipAdder, player, holder, attributeModifier);
                        } else {
                            display.apply(tooltipAdder, player, holder, attributeModifier);
                        }
                    });
        }
    }

    /**
     * Removes the green attribute modifier option.
     *
     * @see ItemAttributeModifiers.Display.Default
     */
    static final class Legacy implements ItemAttributeModifiers.Display {
        static final Legacy INSTANCE = new Legacy();

        @Override
        public Type type() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void apply(Consumer<Component> output, @Nullable Player player, Holder<Attribute> attribute, AttributeModifier modifier) {
            double modifierAmount = modifier.amount();
            if (player != null) {
                if (modifier.is(Item.BASE_ATTACK_DAMAGE_ID)) {
                    modifierAmount += player.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
                } else if (modifier.is(Item.BASE_ATTACK_SPEED_ID)) {
                    modifierAmount += player.getAttributeBaseValue(Attributes.ATTACK_SPEED);
                }
            }

            double scaledModifierAmount;
            if (modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    || modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
                scaledModifierAmount = modifierAmount * 100.0;
            } else if (attribute.is(Attributes.KNOCKBACK_RESISTANCE)) {
                scaledModifierAmount = modifierAmount * 10.0;
            } else {
                scaledModifierAmount = modifierAmount;
            }

            if (modifierAmount > 0.0F) {
                output.accept(Component.translatable("attribute.modifier.plus." + modifier.operation().id(),
                                ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(scaledModifierAmount),
                                Component.translatable(attribute.value().getDescriptionId()))
                        .withStyle(attribute.value().getStyle(true)));
            } else if (modifierAmount < 0.0F) {
                output.accept(Component.translatable("attribute.modifier.take." + modifier.operation().id(),
                                ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(-scaledModifierAmount),
                                Component.translatable(attribute.value().getDescriptionId()))
                        .withStyle(attribute.value().getStyle(false)));
            }
        }
    }
}
