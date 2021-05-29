package com.awakenedredstone.betteritemtooltips.util;

import com.awakenedredstone.betteritemtooltips.BetterItemTooltips;
import com.awakenedredstone.betteritemtooltips.config.Configs;
import com.awakenedredstone.betteritemtooltips.mixin.IHandledScreenMixin;
import com.awakenedredstone.betteritemtooltips.tooltip.TooltipMain;
import fi.dy.masa.malilib.hotkeys.IMouseInputHandler;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.malilib.util.InputUtils;
import fi.dy.masa.malilib.util.KeyCodes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.item.ItemGroup;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;

public class ScrollUtil implements IMouseInputHandler {

    private static final ScrollUtil INSTANCE = new ScrollUtil();

    public static ScrollUtil getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean onMouseScroll(int mouseX, int mouseY, double amount) {
        return this.handleInput(KeyCodes.KEY_NONE, false, amount);
    }

    private boolean handleInput(int keyCode, boolean keyState, double dWheel) {
        if (BetterItemTooltips.disabled) return false;
        MinecraftClient mc = MinecraftClient.getInstance();
        Screen screen = mc.currentScreen;
        boolean cancel = false;
        if (!(screen instanceof HandledScreen) && !(screen instanceof ChatScreen) && !(screen instanceof BookScreen)) {
            return false;
        }

        if (screen instanceof CreativeInventoryScreen) {
            if (((CreativeInventoryScreen) screen).getSelectedTab() != ItemGroup.INVENTORY.getIndex()) {
                return false;
            }
        }

        if (screen instanceof HandledScreen) {
            if (((IHandledScreenMixin) screen).getHoveredSlot() == null) return false;
            if (!((IHandledScreenMixin) screen).getHoveredSlot().hasStack()) {
                return false;
            }
        }

        final int mouseX = InputUtils.getMouseX();
        final int mouseY = InputUtils.getMouseY();

        if (screen instanceof ChatScreen) {
            Style text = mc.inGameHud.getChatHud().getText(mouseX, mouseY);
            if (text == null || text.getHoverEvent() == null) {
                return false;
            } else if (text.getHoverEvent().getValue(HoverEvent.Action.SHOW_ITEM) == null) {
                return false;
            }
        }

        if (screen instanceof BookScreen) {
            Style text = ((BookScreen) screen).getTextAt(mouseX, mouseY);
            if (text == null || text.getHoverEvent() == null) {
                return false;
            } else if (text.getHoverEvent().getValue(HoverEvent.Action.SHOW_ITEM) == null) {
                return false;
            }
        }

        cancel = true;
        TooltipMode tooltipMode = (TooltipMode) Configs.Settings.TOOLTIP_MODE.getOptionListValue();
        if (Configs.Hotkeys.HORIZONTAL_SCROLL.getKeybind().isKeybindHeld()) {
            int multiplier = Configs.Settings.HORIZONTAL_SCROLL_AMOUNT.getIntegerValue();

            if (tooltipMode == TooltipMode.TOOLTIP_SCROLL) TooltipMain.horizontalScroll += -dWheel * multiplier;
            else if (TooltipMain.horizontalScroll + -dWheel * multiplier < 0) TooltipMain.horizontalScroll = 0;
            else if (TooltipMain.horizontalScroll + -dWheel * multiplier > TooltipMain.horizontalScrollMax)
                TooltipMain.horizontalScroll = TooltipMain.horizontalScrollMax;
            else TooltipMain.horizontalScroll += -dWheel * multiplier;
        } else {
            int multiplier = tooltipMode == TooltipMode.TOOLTIP_SCROLL ? Configs.Settings.VERTICAL_SCROLL_AMOUNT.getIntegerValue() : 1;

            if (tooltipMode == TooltipMode.TOOLTIP_SCROLL) TooltipMain.verticalScroll += -dWheel * multiplier;
            else if (TooltipMain.verticalScroll + -dWheel * multiplier < 0) TooltipMain.verticalScroll = 0;
            else if (TooltipMain.verticalScroll + -dWheel * multiplier > TooltipMain.vertScrollMax)
                TooltipMain.verticalScroll = TooltipMain.vertScrollMax;
            else TooltipMain.verticalScroll += -dWheel * multiplier;
        }

        return cancel;
    }
}
