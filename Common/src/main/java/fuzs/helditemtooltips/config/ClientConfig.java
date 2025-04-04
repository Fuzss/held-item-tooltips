package fuzs.helditemtooltips.config;

import com.google.common.collect.Lists;
import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;
import fuzs.puzzleslib.api.config.v3.serialization.ConfigDataSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

import java.util.List;

public class ClientConfig implements ConfigCore {
    static final String COMPONENTS_CATEGORY = "components";

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
    @Config(name = "item_blacklist", description = {"Disables held item tooltips for specified items, mainly to prevent custom tooltips from overlapping.", ConfigDataSet.CONFIG_DESCRIPTION})
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
    @Config(description = "Should the tooltip be hidden when the respective data component is present.")
    public boolean respectHiddenTooltip = true;
    @Config(category = COMPONENTS_CATEGORY, description = "Display name of the held item.")
    public final TooltipComponentConfig itemName = TooltipComponentConfig.simple(true, 10, 100);
    @Config(category = COMPONENTS_CATEGORY, description = "Additional information supplied by individual items such as potion effect and firework duration.")
    public final TooltipComponentConfig additional = TooltipComponentConfig.advanced(true, 20, 90);
    @Config(category = COMPONENTS_CATEGORY, description = "The track stored on a music disc.")
    public final TooltipComponentConfig jukebox = TooltipComponentConfig.simple(true, 22, 45);
    @Config(category = COMPONENTS_CATEGORY, description = "Armor trims applied to this item.")
    public final TooltipComponentConfig trim = TooltipComponentConfig.simple(true, 25, 60);
    @Config(category = COMPONENTS_CATEGORY, description = "All stored enchantments on this enchanted book if any are present.")
    public final TooltipComponentConfig storedEnchantments = TooltipComponentConfig.simple(true, 27, 70);
    @Config(category = COMPONENTS_CATEGORY, description = "All enchantments on this item if any are present.")
    public final TooltipComponentConfig enchantments = TooltipComponentConfig.simple(true, 30, 70);
    @Config(category = COMPONENTS_CATEGORY, description = "The color of dyed items such a leather armor.")
    public final TooltipComponentConfig dyeColor = TooltipComponentConfig.advanced(true, 40, 40);
    @Config(category = COMPONENTS_CATEGORY, description = "A lore tag for this item, only present on custom items.")
    public final TooltipComponentConfig lore = TooltipComponentConfig.simple(true, 50, 50);
    @Config(category = COMPONENTS_CATEGORY, description = "Attributes this item provides when used or equipped, like attack damage and armor protection.")
    public final TooltipComponentConfig attribute = TooltipComponentConfig.simple(false, 60, 30);
    @Config(category = COMPONENTS_CATEGORY, description = "Rendered when this item has the unbreakable tag giving it infinite durability.")
    public final TooltipComponentConfig unbreakable = TooltipComponentConfig.simple(true, 70, 55);
    @Config(category = COMPONENTS_CATEGORY, description = "Durability of this item, only shown if the item is damageable and has been used.")
    public final TooltipComponentConfig durability = TooltipComponentConfig.simple(true, 80, 80);
    @Config(category = COMPONENTS_CATEGORY, description = "Internal identifier of this item.")
    public final TooltipComponentConfig identifier = TooltipComponentConfig.simple(false, 90, 20);
    @Config(category = COMPONENTS_CATEGORY, description = "Amount of nbt tags on this item.")
    public final TooltipComponentConfig componentCount = TooltipComponentConfig.simple(false, 100, 10);

    public ConfigDataSet<Item> itemBlacklist;

    @Override
    public void afterConfigReload() {
        this.itemBlacklist = ConfigDataSet.from(Registries.ITEM, this.itemBlacklistRaw);
    }

    public enum HoverTextBackground {
        NONE, RECTANGLE, ADAPTIVE
    }
}
