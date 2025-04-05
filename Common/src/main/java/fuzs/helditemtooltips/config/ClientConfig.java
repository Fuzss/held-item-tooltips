package fuzs.helditemtooltips.config;

import com.google.common.collect.Lists;
import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;
import fuzs.puzzleslib.api.config.v3.serialization.ConfigDataSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

import java.util.List;

public class ClientConfig implements ConfigCore {
    @Config(description = "Scale of held item tooltips. Works together with \"GUI Scale\" option in \"Video Settings\". A smaller scale might make room for more rows.")
    @Config.IntRange(min = 1, max = 24)
    public int displayScale = 6;
    @Config(description = "Offset on x-axis from screen center.")
    public int offsetX = 0;
    @Config(description = "Offset on y-axis from screen center.")
    @Config.IntRange(min = 0)
    public int offsetY = 59;
    @Config(description = "Amount of ticks the held item tooltip will be displayed for. Set to 0 to always display the tooltip as long as an item is being held.")
    @Config.IntRange(min = 0)
    public int displayTime = 40;
    @Config(description = "Maximum amount of rows to be displayed for held item tooltips.")
    @Config.IntRange(min = 1, max = 12)
    public int maxLines = 4;
    @Config(
            name = "item_blacklist", description = {
            "Disables held item tooltips for specified items, mainly to prevent custom tooltips from overlapping.",
            ConfigDataSet.CONFIG_DESCRIPTION
    }
    )
    List<String> itemBlacklistRaw = Lists.newArrayList();
    @Config(description = "Interval in ticks after which the tooltip will be rebuilt. Some stats such as durability are rebuilt every tick regardless of this setting.")
    @Config.IntRange(min = 1)
    public int updateInterval = 20;
    @Config(description = "Show how many more lines there are that currently don't fit the tooltip, just like the vanilla shulker box tooltip.")
    public boolean lastLine = true;
    @Config(description = "Show black chat background behind tooltip lines for better visibility.")
    public HoverTextBackground background = HoverTextBackground.RECTANGLE;
    @Config(description = "Allow other mods to modify held item tooltip contents. Lines could be both added or changed.")
    public boolean additionalTooltipLines = false;
    @Config(description = "Should the tooltip or individual components on the tooltip be hidden when the \"minecraft:tooltip_display\" component is configured.")
    public boolean respectTooltipDisplayComponent = true;
    @Config(description = "Options for individual tooltip display entries.")
    public final TooltipLinesConfig tooltipLines = new TooltipLinesConfig();

    public ConfigDataSet<Item> itemBlacklist;

    @Override
    public void afterConfigReload() {
        this.itemBlacklist = ConfigDataSet.from(Registries.ITEM, this.itemBlacklistRaw);
    }

    public enum HoverTextBackground {
        NONE,
        RECTANGLE,
        ADAPTIVE
    }

    public static class TooltipLinesConfig implements ConfigCore {
        @Config(description = "Display name of the held item.")
        public final TooltipComponentConfig itemName = TooltipComponentConfig.simple(true, 10, 100);
        @Config(
                description = "Additional information supplied by individual items such as painting and smithing template information."
        )
        public final TooltipComponentConfig additional = TooltipComponentConfig.advanced(true, 20, 80);
        @Config(description = "The id of a map.")
        public final TooltipComponentConfig components = TooltipComponentConfig.simple(true, 30, 70);
        @Config(
                description = "Attributes this item provides when used or equipped, like attack damage and armor protection."
        )
        public final TooltipComponentConfig attributeModifiers = TooltipComponentConfig.simple(false, 50, 50);
        @Config(
                description = "Durability of this item, only shown if the item is damageable and has been used."
        )
        public final TooltipComponentConfig durability = TooltipComponentConfig.simple(true, 70, 90);
        @Config(description = "Internal identifier of this item.")
        public final TooltipComponentConfig identifier = TooltipComponentConfig.simple(false, 80, 30);
        @Config(description = "Amount of components on this item.")
        public final TooltipComponentConfig componentCount = TooltipComponentConfig.simple(false, 90, 20);
        @Config(description = "A warning shown for an unavailable experimental item.")
        public final TooltipComponentConfig disabled = TooltipComponentConfig.simple(true, 100, 10);
    }
}
