package fuzs.helditemtooltips.common.client.gui.screens.inventory.tooltip.component;

import com.google.common.collect.ImmutableMap;
import fuzs.helditemtooltips.common.HeldItemTooltips;
import fuzs.helditemtooltips.common.client.handler.SelectedItemHandler;
import fuzs.helditemtooltips.common.config.ClientConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Unit;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public final class ComponentsTooltipComponent implements TooltipComponent {
    private static final Component UNBREAKABLE_TOOLTIP = Component.translatable("item.unbreakable")
            .withStyle(ChatFormatting.BLUE);
    private static final TooltipProviderExtractor<ItemContainerContents> CONTAINER = (ItemContainerContents itemContainerContents) -> {
        return (Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter) -> {
            // Add all lines separately so that our last line shows the correct number for cut-off lines (important when handling shulker boxes).
            Stream<ItemStack> displayItems;
            if (HeldItemTooltips.CONFIG.get(ClientConfig.class).tooltipLines.components.combineContainerContents) {
                Map<Holder<Item>, ItemStackTemplate> templatesByType = new LinkedHashMap<>();
                for (ItemStackTemplate template : itemContainerContents.nonEmptyItems()) {
                    templatesByType.merge(template.typeHolder(),
                            template,
                            (ItemStackTemplate previousTemplate, ItemStackTemplate updatedTemplate) -> {
                                return previousTemplate.withCount(previousTemplate.count() + updatedTemplate.count());
                            });
                }

                displayItems = templatesByType.values().stream().map((ItemStackTemplate template) -> {
                    return new ItemStack(template.typeHolder(), template.count());
                });
            } else {
                displayItems = itemContainerContents.nonEmptyItemCopyStream();
            }

            displayItems.map((ItemStack itemStack) -> {
                return Component.translatable("item.container.item_count",
                        itemStack.getHoverName(),
                        itemStack.getCount()).withStyle(ChatFormatting.GRAY);
            }).forEach(consumer);
        };
    };
    private static final TooltipProviderExtractor<ArmorTrim> TRIM = (ArmorTrim armorTrim) -> {
        return (Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter) -> {
            List<Component> tooltipLines = new ArrayList<>();
            armorTrim.addToTooltip(tooltipContext, tooltipLines::add, tooltipFlag, dataComponentGetter);
            if (tooltipLines.size() == 3) {
                // the armor trim type line is enough, it has the color of the material
                consumer.accept(tooltipLines.get(1));
            } else {
                tooltipLines.forEach(consumer);
            }
        };
    };
    private static final TooltipProviderExtractor<Unit> UNBREAKABLE = (Unit unit) -> {
        return (Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter) -> {
            consumer.accept(UNBREAKABLE_TOOLTIP);
        };
    };
    private static final TooltipProviderExtractor<AdventureModePredicate> CAN_BREAK = (AdventureModePredicate adventureModePredicate) -> {
        return (Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter) -> {
            consumer.accept(AdventureModePredicate.CAN_BREAK_HEADER);
            adventureModePredicate.addToTooltip(consumer);
        };
    };
    private static final TooltipProviderExtractor<AdventureModePredicate> CAN_PLACE = (AdventureModePredicate adventureModePredicate) -> {
        return (Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter) -> {
            consumer.accept(AdventureModePredicate.CAN_PLACE_HEADER);
            adventureModePredicate.addToTooltip(consumer);
        };
    };
    private static final Map<DataComponentType<?>, TooltipProviderExtractor<?>> TOOLTIP_PROVIDER_EXTRACTORS;

    static {
        ImmutableMap.Builder<DataComponentType<?>, TooltipProviderExtractor<?>> builder = ImmutableMap.builder();
        builder.put(DataComponents.CONTAINER, CONTAINER);
        builder.put(DataComponents.TRIM, TRIM);
        builder.put(DataComponents.UNBREAKABLE, UNBREAKABLE);
        builder.put(DataComponents.CAN_BREAK, CAN_BREAK);
        builder.put(DataComponents.CAN_PLACE_ON, CAN_PLACE);
        TOOLTIP_PROVIDER_EXTRACTORS = builder.build();
    }

    @Override
    public void addToTooltip(ItemStack itemStack, Item.TooltipContext tooltipContext, Consumer<Component> componentConsumer, TooltipFlag tooltipFlag) {
        TooltipDisplay tooltipDisplay = SelectedItemHandler.getTooltipDisplayComponent(itemStack);
        for (TypedDataComponent<?> component : itemStack.getComponents()) {
            if (tooltipDisplay.shows(component.type())) {
                TooltipProvider tooltipProvider = TOOLTIP_PROVIDER_EXTRACTORS.getOrDefault(component.type(),
                        TooltipProviderExtractor.DEFAULT).applyAsType(component.value());
                if (tooltipProvider != null) {
                    tooltipProvider.addToTooltip(tooltipContext,
                            componentConsumer,
                            tooltipFlag,
                            itemStack.getComponents());
                }
            }
        }
    }

    @FunctionalInterface
    private interface TooltipProviderExtractor<T> extends Function<T, @Nullable TooltipProvider> {
        TooltipProviderExtractor<?> DEFAULT = (Object o) -> {
            return o instanceof TooltipProvider tooltipProvider ? tooltipProvider : null;
        };

        default @Nullable TooltipProvider applyAsType(Object o) {
            return this.apply((T) o);
        }
    }
}
