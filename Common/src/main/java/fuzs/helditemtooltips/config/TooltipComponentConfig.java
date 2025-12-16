package fuzs.helditemtooltips.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import org.jspecify.annotations.Nullable;

public class TooltipComponentConfig implements ConfigCore {
    @Config(description = "Should this tooltip component be included when rendering held item tooltips.")
    public boolean include;
    @Config(description = "The order in which this tooltip component appears in relation to other components. Smaller values appear first.")
    public int ordering;
    @Config(description = "Priority for rendering this tooltip. When not enough lines are available (like an item with enchantments), components with a low priority will be skipped.")
    public int priority;
    @Config(description = "Represent information for this component as if advanced tooltips were enabled independently of the actual setting.")
    public boolean advancedTooltips;
    @Config(description = "Text formatting settings for this component's text appearance.")
    private final FormattingConfig formatting;

    private TooltipComponentConfig(boolean defaultValue, int ordering, int priority, boolean advancedTooltips, @Nullable ChatFormatting textColor) {
        this.include = defaultValue;
        this.ordering = ordering;
        this.priority = priority;
        this.advancedTooltips = advancedTooltips;
        this.formatting = new FormattingConfig(textColor);
    }

    public static TooltipComponentConfig simple(boolean defaultValue, int ordering, int priority) {
        return new TooltipComponentConfig(defaultValue, ordering, priority, false, null);
    }

    public static TooltipComponentConfig advanced(boolean defaultValue, int ordering, int priority) {
        return new TooltipComponentConfig(defaultValue, ordering, priority, true, null);
    }

    public Style getStyle() {
        return this.formatting.getStyle();
    }

    private static class FormattingConfig implements ConfigCore {
        static final String DEFAULT_FORMATTING = "default";

        @Config(name = "text_color", description = "The color of this component's text.")
        @Config.AllowedValues(
                values = {
                        DEFAULT_FORMATTING,
                        "black",
                        "dark_blue",
                        "dark_green",
                        "dark_aqua",
                        "dark_red",
                        "dark_purple",
                        "gold",
                        "gray",
                        "dark_gray",
                        "blue",
                        "green",
                        "aqua",
                        "red",
                        "light_purple",
                        "yellow",
                        "white"
                }
        )
        String textColorRaw;
        @Config(description = "Should the text in this component be replaced by random characters.")
        public boolean obfuscated;
        @Config(description = "Should the text in this component appear bold.")
        public boolean bold;
        @Config(description = "Should the text in this component appear struck-through.")
        public boolean strikethrough;
        @Config(description = "Should the text in this component appear with an underline.")
        public boolean underline;
        @Config(description = "Should the text in this component appear italic.")
        public boolean italic;

        @Nullable
        public ChatFormatting textColor;

        public FormattingConfig(@Nullable ChatFormatting textColor) {
            this.textColorRaw = textColor == null ? DEFAULT_FORMATTING : textColor.getSerializedName();
        }

        @Override
        public void afterConfigReload() {
            this.textColor = ChatFormatting.getByName(this.textColorRaw);
        }

        Style getStyle() {
            Style style = Style.EMPTY;
            if (this.textColor != null) style = style.withColor(this.textColor);
            if (this.obfuscated) style = style.withObfuscated(true);
            if (this.bold) style = style.withBold(true);
            if (this.strikethrough) style = style.withStrikethrough(true);
            if (this.underline) style = style.withUnderlined(true);
            if (this.italic) style = style.withItalic(true);
            return style;
        }
    }
}
