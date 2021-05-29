package com.awakenedredstone.betteritemtooltips.util;

import com.awakenedredstone.betteritemtooltips.BetterItemTooltips;
import com.awakenedredstone.betteritemtooltips.config.Configs;
import com.awakenedredstone.betteritemtooltips.mixin.IHandledScreenMixin;
import com.awakenedredstone.betteritemtooltips.tooltip.TooltipMain;
import fi.dy.masa.malilib.hotkeys.IMouseInputHandler;
import fi.dy.masa.malilib.util.InputUtils;
import fi.dy.masa.malilib.util.KeyCodes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemGroup;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;

public class MouseCallback implements IMouseInputHandler {

    private static final ScrollUtil INSTANCE = new ScrollUtil();
    public static boolean manipulateNbt = false;
    private static boolean isDragging;

    public static ScrollUtil getInstance() {
        return INSTANCE;
    }

    private final MinecraftClient mc;

    public MouseCallback(MinecraftClient mc) {
        this.mc = mc;
    }

    @Override
    public void onMouseMove(int mouseX, int mouseY) {
        this.handleMovement(mouseX, mouseY);
    }

    @Override
    public boolean onMouseClick(int mouseX, int mouseY, int eventButton, boolean eventButtonState) {
        if (eventButton == 0) {
            isDragging = eventButtonState;
            return true;
        }
        return false;
    }

    private void handleMovement(int mouseX, int mouseY) {
        Screen screen = mc.currentScreen;
        if (!(screen instanceof HandledScreen || screen instanceof ChatScreen || screen instanceof BookScreen)) {
            return;
        }
    }
}
