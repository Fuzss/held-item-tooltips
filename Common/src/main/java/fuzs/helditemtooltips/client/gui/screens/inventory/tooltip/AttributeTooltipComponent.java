package fuzs.helditemtooltips.client.gui.screens.inventory.tooltip;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public final class AttributeTooltipComponent implements TooltipComponent {

    @Override
    public void addToTooltip(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> tooltipLines, TooltipFlag tooltipFlag) {
        Minecraft minecraft = Minecraft.getInstance();
        // collects attribute modifiers in a condensed format: equipment group lines are skipped, the color format is simplified (only red & blue)
        this.addAttributeTooltips(itemStack, tooltipLines::add, minecraft.player);
    }

    /**
     * Copied from {@link ItemStack#addAttributeTooltips(Consumer, Player)} with slot descriptions being removed.
     */
    private void addAttributeTooltips(ItemStack itemStack, Consumer<Component> tooltipAdder, @Nullable Player player) {
        ItemAttributeModifiers itemAttributeModifiers = itemStack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
        if (itemAttributeModifiers.showInTooltip()) {
            for (EquipmentSlotGroup equipmentSlotGroup : EquipmentSlotGroup.values()) {
                itemStack.forEachModifier(equipmentSlotGroup, (Holder<Attribute> holder, AttributeModifier attributeModifier) -> {
                    this.addModifierTooltip(tooltipAdder, player, holder, attributeModifier);
                });
            }
        }
    }

    /**
     * Copied from {@link ItemStack#addModifierTooltip(Consumer, Player, Holder, AttributeModifier)} with the green attribute modifier option being removed.
     */
    private void addModifierTooltip(Consumer<Component> tooltipAdder, @Nullable Player player, Holder<Attribute> attribute, AttributeModifier modifier) {
        double modifierAmount = modifier.amount();
        if (player != null) {
            if (modifier.is(Item.BASE_ATTACK_DAMAGE_ID)) {
                modifierAmount += player.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
            } else if (modifier.is(Item.BASE_ATTACK_SPEED_ID)) {
                modifierAmount += player.getAttributeBaseValue(Attributes.ATTACK_SPEED);
            }
        }

        double scaledModifierAmount;
        if (modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_BASE || modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
            scaledModifierAmount = modifierAmount * 100.0;
        } else if (attribute.is(Attributes.KNOCKBACK_RESISTANCE)) {
            scaledModifierAmount = modifierAmount * 10.0;
        } else {
            scaledModifierAmount = modifierAmount;
        }

        if (modifierAmount > 0.0) {
            tooltipAdder.accept(
                    Component.translatable(
                                    "attribute.modifier.plus." + modifier.operation().id(),
                                    ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(scaledModifierAmount),
                                    Component.translatable(attribute.value().getDescriptionId())
                            )
                            .withStyle(attribute.value().getStyle(true))
            );
        } else if (modifierAmount < 0.0) {
            tooltipAdder.accept(
                    Component.translatable(
                                    "attribute.modifier.take." + modifier.operation().id(),
                                    ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(-scaledModifierAmount),
                                    Component.translatable(attribute.value().getDescriptionId())
                            )
                            .withStyle(attribute.value().getStyle(false))
            );
        }
    }
}
