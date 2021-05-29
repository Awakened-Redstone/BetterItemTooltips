package com.awakenedredstone.betteritemtooltips.tooltip;

import com.awakenedredstone.betteritemtooltips.config.Configs;
import com.awakenedredstone.betteritemtooltips.util.TooltipMode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.awakenedredstone.betteritemtooltips.BetterItemTooltips.disabled;

public class TooltipMain {

    public static int verticalScroll = 0;
    public static int horizontalScroll = 0;

    public static int vertScrollMax = 999;
    public static int horizontalScrollMax = 999;

    public static CompoundTag lastNBT;
    public static boolean lastItemHaveNbt = false;
    private static ItemStack lastItem;

    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static void modifyTooltip(ItemStack stack, TooltipContext context, List<Text> tooltipOrig) {
        if (disabled) return;
        List<Text> tooltip = new ArrayList<>();
        if (!Configs.Settings.PERSISTENT_TOOLTIP.getBooleanValue() && lastItem != null && stack != null && lastItem.getItem() != stack.getItem() && !stack.isEmpty()) {
            verticalScroll = 0;
            horizontalScroll = 0;
            lastItem = stack;
        } else if (!Configs.Settings.PERSISTENT_TOOLTIP.getBooleanValue() && (stack == null || stack.isEmpty())) {
            verticalScroll = 0;
            horizontalScroll = 0;
            lastItemHaveNbt = false;
        }
        if (Configs.Settings.ALWAYS_SHOW.getBooleanValue() || Configs.Hotkeys.SHOW.getKeybind().isKeybindHeld()) {
            if (!Configs.Hotkeys.SHOW_ONLY_NBT.getKeybind().isKeybindHeld()) tooltip.add(new LiteralText(""));
            if (stack != null) {
                if (!stack.hasTag()) {
                    tooltip.add(new LiteralText("No NBT tag").formatted(Formatting.DARK_GRAY));
                    lastItemHaveNbt = false;
                } else {
                    lastItemHaveNbt = true;
                    if (Configs.Settings.REMOVE_DEBUG_LINE.getBooleanValue())
                        tooltipOrig.removeIf(c -> c.getClass() == TranslatableText.class && ((TranslatableText) c).getKey().equals("item.nbt_tags"));
                    if (lastNBT != stack.getTag()) {
                        lastNBT = stack.getTag();
                    }
                    if (lastNBT != null) {
                        int tooltipMaxWidth = 0;
                        for (Text orderedText : tooltipOrig) {
                            int j = client.textRenderer.getWidth(orderedText);
                            if (j > tooltipMaxWidth) {
                                tooltipMaxWidth = j;
                            }
                        }

                        int nbtMinWidth = Configs.Settings.NBT_MIN_WIDTH.getIntegerValue();

                        if (tooltipMaxWidth < nbtMinWidth) {
                            tooltipMaxWidth = nbtMinWidth;
                        }

                        Text text = lastNBT.toText();
                        List<StringVisitable> visitables = client.textRenderer.getTextHandler().wrapLines(text, tooltipMaxWidth, Style.EMPTY);
                        for (StringVisitable stringVisitable : visitables) {
                            MutableText mutableText = LiteralText.EMPTY.shallowCopy();
                            stringVisitable.visit((style, string) -> {
                                mutableText.append(new LiteralText(string).setStyle(style));
                                return Optional.empty();
                            }, Style.EMPTY);
                            tooltip.add(mutableText);
                        }
                    }
                }
            }
        } else if (Configs.Settings.TOOLTIP_MODE.getOptionListValue() == TooltipMode.TEXT_SCROLL) {
            if (verticalScroll > vertScrollMax) verticalScroll = vertScrollMax;
            if (horizontalScroll > horizontalScrollMax) horizontalScroll = horizontalScrollMax;
        }
        polishTooltip(stack, context, tooltip, tooltipOrig);
    }

    public static void polishTooltip(ItemStack stack, TooltipContext context, List<Text> newTooltip, List<Text> tooltip) {
        TooltipMode tooltipMode = (TooltipMode) Configs.Settings.TOOLTIP_MODE.getOptionListValue();
        int maxLines = Configs.Settings.MAX_LINES_SHOWN.getIntegerValue();
        List<Text> oldTooltip = new ArrayList<>();

        boolean showNbt = Configs.Settings.ALWAYS_SHOW.getBooleanValue() || Configs.Hotkeys.SHOW.getKeybind().isKeybindHeld();
        if (!Configs.Hotkeys.SHOW_ONLY_NBT.getKeybind().isKeybindHeld() || !showNbt) oldTooltip.addAll(tooltip);
        if (!newTooltip.isEmpty()) oldTooltip.addAll(newTooltip);

        vertScrollMax = Math.max((tooltip.size() + newTooltip.size()) - maxLines, 0);

        List<Text> finalTooltip = new ArrayList<>();

        if (tooltipMode == TooltipMode.TEXT_SCROLL) {
            for (int i = 0; i < maxLines; i++) {
                if (oldTooltip.size() <= i + verticalScroll) break;
                finalTooltip.add(oldTooltip.get(i + verticalScroll));
            }
        } else {
            finalTooltip.addAll(oldTooltip);
        }

        tooltip.clear();
        tooltip.addAll(finalTooltip);
    }
}
