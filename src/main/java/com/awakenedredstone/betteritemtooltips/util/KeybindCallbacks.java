package com.awakenedredstone.betteritemtooltips.util;

import com.awakenedredstone.betteritemtooltips.BetterItemTooltips;
import com.awakenedredstone.betteritemtooltips.mixin.IHandledScreenMixin;
import com.awakenedredstone.betteritemtooltips.tooltip.TooltipMain;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import com.awakenedredstone.betteritemtooltips.config.Configs;
import com.awakenedredstone.betteritemtooltips.gui.GuiConfigs;
import fi.dy.masa.malilib.util.InputUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;

import static com.awakenedredstone.betteritemtooltips.BetterItemTooltips.disabled;

public class KeybindCallbacks implements IHotkeyCallback {

    private final MinecraftClient mc;

    public KeybindCallbacks(MinecraftClient mc) {
        this.mc = mc;
    }

    @Override
    public boolean onKeyAction(KeyAction keyAction, IKeybind key) {
        if (mc.player == null || mc.world == null) {
            return false;
        }

        if (key == Configs.Hotkeys.KEY_OPEN_CONFIG_GUI.getKeybind()) {
            GuiBase.openGui(new GuiConfigs());
            return true;
        }

        if (key == Configs.Hotkeys.KEY_MAIN_TOGGLE.getKeybind()) {
            disabled = !disabled;
            if (disabled) {
                mc.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASS, 0.8f, 0.8f);
            } else {
                mc.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_PLING, 0.5f, 1.0f);
            }

            return true;
        }

        if (disabled) return false;
        Screen screen = mc.currentScreen;
        if (key == Configs.Hotkeys.COPY.getKeybind()) {
            String pattern = Configs.Settings.COPY_PATTERN.getStringValue();
            SystemToast.Type toastType = SystemToast.Type.TUTORIAL_HINT;
            if (screen instanceof HandledScreen || screen instanceof ChatScreen || screen instanceof BookScreen) {
                if (screen instanceof HandledScreen) {
                    if (((IHandledScreenMixin) screen).getHoveredSlot().hasStack()) {
                        ItemStack item = ((IHandledScreenMixin) screen).getHoveredSlot().getStack();
                        mc.keyboard.setClipboard(PatternParser.parse(pattern, item));
                        mc.getToastManager().add(new SystemToast(toastType, new TranslatableText("betteritemtooltips.copied_to_clipboard"), new TranslatableText("betteritemtooltips.object_details", item.getItem().getName())));
                        return true;
                    }
                }

                final int mouseX = InputUtils.getMouseX();
                final int mouseY = InputUtils.getMouseY();

                if (screen instanceof ChatScreen) {
                    Style text = mc.inGameHud.getChatHud().getText(mouseX, mouseY);
                    if (text != null && text.getHoverEvent() != null && text.getHoverEvent().getValue(HoverEvent.Action.SHOW_ITEM) != null) {
                        ItemStack item = text.getHoverEvent().getValue(HoverEvent.Action.SHOW_ITEM).asStack();
                        mc.keyboard.setClipboard(PatternParser.parse(pattern, item));
                        mc.getToastManager().add(new SystemToast(toastType, new TranslatableText("betteritemtooltips.copied_to_clipboard"), new TranslatableText("betteritemtooltips.object_details", item.getItem().getName())));
                        return true;
                    }
                }

                if (screen instanceof BookScreen) {
                    Style text = ((BookScreen) screen).getTextAt(mouseX, mouseY);
                    if (text != null && text.getHoverEvent() != null && text.getHoverEvent().getValue(HoverEvent.Action.SHOW_ITEM) != null) {
                        ItemStack item = text.getHoverEvent().getValue(HoverEvent.Action.SHOW_ITEM).asStack();
                        mc.keyboard.setClipboard(PatternParser.parse(pattern, item));
                        mc.getToastManager().add(new SystemToast(toastType, new TranslatableText("betteritemtooltips.copied_to_clipboard"), new TranslatableText("betteritemtooltips.object_details", item.getItem().getName())));
                        return true;
                    }
                }
            }
        }

        if (!(screen instanceof HandledScreen || screen instanceof ChatScreen || screen instanceof BookScreen)) {
            return false;
        }

        if (key == Configs.Hotkeys.RESET_SCROLL.getKeybind()) {
            TooltipMain.verticalScroll = 0;
            TooltipMain.horizontalScroll = 0;
            return true;
        }

        if (key == Configs.Hotkeys.MANIPULATE_NBT.getKeybind()) {
            MouseCallback.manipulateNbt = !MouseCallback.manipulateNbt;
            return true;
        }

        return false;
    }
}
