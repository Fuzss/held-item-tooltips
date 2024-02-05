package fuzs.helditemtooltips.client.gui.screens.inventory.tooltip;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ModifiersTooltipComponent implements TooltipComponent {
    protected static final UUID BASE_ATTACK_DAMAGE_UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    protected static final UUID BASE_ATTACK_SPEED_UUID = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");

    @Override
    public void appendTooltipLines(List<Component> lines, ItemStack stack, @Nullable Player player, TooltipFlag tooltipFlag, Style style) {
        List<Component> list = Lists.newArrayList();
        addOldStyleAttributes(list, stack, player, 0);
        for (Component component : list) {
            lines.add(Component.empty().append(component).withStyle(style.applyTo(component.getStyle())));
        }
    }

    private static void addOldStyleAttributes(List<Component> list, ItemStack stack, @Nullable Player player, int startIndex) {
        final Map<EquipmentSlot, Multimap<Attribute, AttributeModifier>> map = getSlotToAttributeMap(stack);
        if (map.size() == 1) {
            List<Component> tmpList = Lists.newArrayList();
            addAttributesToTooltip(tmpList, player, stack, map.values().iterator().next());
            if (!tmpList.isEmpty()) {
                if (startIndex != 0) tmpList.add(0, Component.empty());
                addListToTooltip(list, tmpList, startIndex);
            }
        } else if (map.size() > 1) {
            int lastSize = 0;
            for (Map.Entry<EquipmentSlot, Multimap<Attribute, AttributeModifier>> entry : map.entrySet()) {
                List<Component> tmpList = Lists.newArrayList();
                addAttributesToTooltip(tmpList, player, stack, entry.getValue());
                if (!tmpList.isEmpty()) {
                    if (startIndex != 0) tmpList.add(0, Component.empty());
                    tmpList.add(1, Component.translatable("item.modifiers." + entry.getKey().getName()).withStyle(ChatFormatting.GRAY));
                    lastSize += tmpList.size();
                    addListToTooltip(list, tmpList, startIndex + lastSize);
                }
            }
        }
    }

    private static void addListToTooltip(List<Component> list, List<Component> tmpList, int startIndex) {
        if (startIndex < list.size()) {
            list.addAll(startIndex, tmpList);
        } else {
            list.addAll(tmpList);
        }
    }

    private static Map<EquipmentSlot, Multimap<Attribute, AttributeModifier>> getSlotToAttributeMap(ItemStack stack) {
        final Map<EquipmentSlot, Multimap<Attribute, AttributeModifier>> map = Maps.newHashMap();
        for (EquipmentSlot equipmentslot : EquipmentSlot.values()) {
            Multimap<Attribute, AttributeModifier> multimap = stack.getAttributeModifiers(equipmentslot);
            if (!multimap.isEmpty()) {
                map.put(equipmentslot, multimap);
            }
        }
        return map;
    }

    private static void addAttributesToTooltip(List<Component> list, @Nullable Player player, ItemStack stack, Multimap<Attribute, AttributeModifier> multimap) {
        for (Map.Entry<Attribute, AttributeModifier> entry : multimap.entries()) {
            AttributeModifier attributemodifier = entry.getValue();
            double d0 = attributemodifier.getAmount();
            boolean flag = false;
            if (player != null) {
                if (attributemodifier.getId().equals(BASE_ATTACK_DAMAGE_UUID)) {
                    d0 += player.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
                    d0 += EnchantmentHelper.getDamageBonus(stack, MobType.UNDEFINED);
                    flag = true;
                } else if (attributemodifier.getId().equals(BASE_ATTACK_SPEED_UUID)) {
                    d0 += player.getAttributeBaseValue(Attributes.ATTACK_SPEED);
                    flag = true;
                }
            }
            double d1;
            if (attributemodifier.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && attributemodifier.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
                if (entry.getKey().equals(Attributes.KNOCKBACK_RESISTANCE)) {
                    d1 = d0 * 10.0D;
                } else {
                    d1 = d0;
                }
            } else {
                d1 = d0 * 100.0D;
            }
            if (flag && d0 != 0.0 || d0 > 0.0D) {
                list.add((Component.translatable("attribute.modifier.plus." + attributemodifier.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), Component.translatable(entry.getKey().getDescriptionId()))).withStyle(ChatFormatting.BLUE));
            } else if (d0 < 0.0D) {
                d1 *= -1.0D;
                list.add((Component.translatable("attribute.modifier.take." + attributemodifier.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), Component.translatable(entry.getKey().getDescriptionId()))).withStyle(ChatFormatting.RED));
            }
        }
    }
}
