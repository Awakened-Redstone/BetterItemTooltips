package com.awakenedredstone.betteritemtooltips.config.options;

import fi.dy.masa.malilib.config.options.ConfigInteger;
import fi.dy.masa.malilib.gui.GuiBase;

public class ConfigIntegerWarning extends ConfigInteger {
    private final String warning;

    public ConfigIntegerWarning(String name, int defaultValue, String comment, String warning) {
        super(name, defaultValue, comment);
        this.warning = warning;
    }

    public ConfigIntegerWarning(String name, int defaultValue, int minValue, int maxValue, String comment, String warning) {
        super(name, defaultValue, minValue, maxValue, comment);
        this.warning = warning;
    }

    public ConfigIntegerWarning(String name, int defaultValue, int minValue, int maxValue, boolean useSlider, String comment, String warning) {
        super(name, defaultValue, minValue, maxValue, useSlider, comment);
        this.warning = warning;
    }

    @Override
    public String getComment() {
        String comment = super.getComment();

        if (comment == null)
        {
            return "";
        }

        return comment + "\n" + GuiBase.TXT_RED + warning + GuiBase.TXT_RST;
    }

    @Override
    public String getConfigGuiDisplayName() {
        return GuiBase.TXT_RED + this.getName() + GuiBase.TXT_RST;
    }
}
