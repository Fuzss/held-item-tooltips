package fuzs.helditemtooltips.common.config;

import fuzs.puzzleslib.common.api.config.v3.Config;
import fuzs.puzzleslib.common.api.config.v3.ConfigCore;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.TooltipFlag;

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
    private final FormattingConfig formatting = new FormattingConfig();

    private TooltipComponentConfig(boolean defaultValue, int ordering, int priority, boolean advancedTooltips) {
        this.include = defaultValue;
        this.ordering = ordering;
        this.priority = priority;
        this.advancedTooltips = advancedTooltips;
    }

    /**
     * @see TooltipFlag#NORMAL
     */
    public static TooltipComponentConfig normal(boolean defaultValue, int ordering, int priority) {
        return new TooltipComponentConfig(defaultValue, ordering, priority, false);
    }

    /**
     * @see TooltipFlag#ADVANCED
     */
    public static TooltipComponentConfig advanced(boolean defaultValue, int ordering, int priority) {
        return new TooltipComponentConfig(defaultValue, ordering, priority, true);
    }

    public TooltipFlag tooltipFlag() {
        return this.advancedTooltips ? TooltipFlag.ADVANCED : TooltipFlag.NORMAL;
    }

    public Style style() {
        return this.formatting.composeStyle();
    }

    private static class FormattingConfig implements ConfigCore {
        @Config(description = "Should the text in this component appear with a custom color.")
        public boolean useTextColor;
        @Config(description = "The color of this component's text if enabled.")
        public DyeColor textColor = DyeColor.WHITE;
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

        Style composeStyle() {
            Style style = Style.EMPTY;
            if (this.useTextColor) {
                style = style.withColor(this.textColor.getTextColor());
            }

            if (this.obfuscated) {
                style = style.withObfuscated(true);
            }

            if (this.bold) {
                style = style.withBold(true);
            }

            if (this.strikethrough) {
                style = style.withStrikethrough(true);
            }

            if (this.underline) {
                style = style.withUnderlined(true);
            }

            if (this.italic) {
                style = style.withItalic(true);
            }

            return style;
        }
    }
}
