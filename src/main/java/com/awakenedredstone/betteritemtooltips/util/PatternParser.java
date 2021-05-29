package com.awakenedredstone.betteritemtooltips.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PatternParser {

    public static String parse(String pattern, ItemStack item) {
        String output = pattern;
        Identifier identifier = Registry.ITEM.getId(item.getItem());
        output = output.replace("{item.namespace}", identifier.getNamespace());
        output = output.replace("{item.path}", identifier.getPath());
        output = output.replace("{item.id}", identifier.toString());
        output = output.replace("{item.nbt}", item.getTag() != null ? item.getTag().toString() : "{}");
        output = output.replace("{item.count}", String.valueOf(item.getCount()));
        output = output.replace("{item.maxCount}", String.valueOf(item.getMaxCount()));
        output = output.replace("{item.displayName}", item.getName().getString());
        return output;
    }
}
