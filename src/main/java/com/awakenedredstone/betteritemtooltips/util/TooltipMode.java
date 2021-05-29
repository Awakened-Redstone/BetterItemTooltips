package com.awakenedredstone.betteritemtooltips.util;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.util.StringUtils;

public enum TooltipMode implements IConfigOptionListEntry
{
    TEXT_SCROLL("textScroll","betteritemtooltips.label.tooltip_mode.text"),
    TOOLTIP_SCROLL("tooltipScroll","betteritemtooltips.label.tooltip_mode.tooltip");

    private final String configString;
    private final String translationKey;

    private TooltipMode(String configString, String translationKey)
    {
        this.configString = configString;
        this.translationKey = translationKey;
    }

    @Override
    public String getStringValue()
    {
        return this.configString;
    }

    @Override
    public String getDisplayName()
    {
        return StringUtils.translate(this.translationKey);
    }

    @Override
    public IConfigOptionListEntry cycle(boolean forward)
    {
        int id = this.ordinal();

        if (forward)
        {
            if (++id >= values().length)
            {
                id = 0;
            }
        }
        else
        {
            if (--id < 0)
            {
                id = values().length - 1;
            }
        }

        return values()[id % values().length];
    }

    @Override
    public TooltipMode fromString(String name)
    {
        return fromStringStatic(name);
    }

    public static TooltipMode fromStringStatic(String name)
    {
        for (TooltipMode mode : TooltipMode.values())
        {
            if (mode.configString.equalsIgnoreCase(name))
            {
                return mode;
            }
        }

        return TooltipMode.TEXT_SCROLL;
    }
}
