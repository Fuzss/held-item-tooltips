package fuzs.helditemtooltips.client.util;

import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ShulkerHoverTextUtil {

    public static void appendHoverText(List<Component> lines, ItemStack stack, int rows, boolean lastLine) {

        if (rows == 0) return;
        CompoundTag tag = stack.getTagElement("BlockEntityTag");
        if (tag == null) return;

        appendEmptyText(lines, tag);
        List<ItemStack> contents = loadAllItems(tag);
        if (contents.size() > rows && rows != -1) {

            for (ItemStack itemstack : contents.subList(0, rows - 1)) {

                MutableComponent component = itemstack.getHoverName().copy();
                lines.add(component.append(" x").append(String.valueOf(itemstack.getCount())));
            }

            if (lastLine) {

                lines.add(Component.translatable("container.shulkerBox.more", contents.size() - rows + 1).withStyle(ChatFormatting.ITALIC));
            }
        } else {

            for (ItemStack content : contents) {

                MutableComponent component = content.getHoverName().copy();
                component.append(" x").append(String.valueOf(content.getCount()));
                lines.add(component);
            }
        }
    }

    private static void appendEmptyText(List<Component> lines, CompoundTag tag) {

        if (tag.contains("LootTable", 8)) {

            lines.add(Component.literal("???????"));
        }
    }

    private static List<ItemStack> loadAllItems(CompoundTag tag) {

        if (tag.contains("Items", 9)) {

            NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
            ContainerHelper.loadAllItems(tag, nonnulllist);
            return mergeSameItems(nonnulllist);
        }

        return Lists.newArrayList();
    }

    private static List<ItemStack> mergeSameItems(List<ItemStack> items) {

        List<ItemStack> contents = Lists.newArrayList();
        for (ItemStack current : items) {

            if (contents.stream().anyMatch(it -> ItemStack.isSameItem(it, current))) {

                contents.forEach(stack -> {
                    if (ItemStack.isSameItem(stack, current)) {

                        stack.setCount(stack.getCount() + current.getCount());
                    }
                });

            } else if (!current.isEmpty()) {

                contents.add(current);
            }
        }

        return contents;
    }

}
