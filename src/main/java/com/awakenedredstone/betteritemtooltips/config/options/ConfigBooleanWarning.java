package com.awakenedredstone.betteritemtooltips.config.options;

import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.gui.GuiBase;

public class ConfigBooleanWarning extends ConfigBoolean {
    private final String warning;

    public ConfigBooleanWarning(String name, boolean defaultValue, String comment, String warning) {
        super(name, defaultValue, comment);
        this.warning = warning;
    }

    public ConfigBooleanWarning(String name, boolean defaultValue, String comment, String prettyName, String warning) {
        super(name, defaultValue, comment, prettyName);
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
