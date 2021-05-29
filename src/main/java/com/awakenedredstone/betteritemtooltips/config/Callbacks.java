package com.awakenedredstone.betteritemtooltips.config;

import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.interfaces.IValueChangeCallback;
import com.awakenedredstone.betteritemtooltips.gui.GuiConfigs;
import com.awakenedredstone.betteritemtooltips.util.KeybindCallbacks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

public class Callbacks {

    private static MinecraftClient mc_;

    public static void init(MinecraftClient mc) {
        mc_ = mc;
        ChangeCallback changeCallback = new ChangeCallback();
        KeybindCallbacks keybindCallback = new KeybindCallbacks(mc);
        Configs.Hotkeys.KEY_OPEN_CONFIG_GUI.getKeybind().setCallback(keybindCallback);
        Configs.Hotkeys.KEY_MAIN_TOGGLE.getKeybind().setCallback(keybindCallback);
        Configs.Hotkeys.RESET_SCROLL.getKeybind().setCallback(keybindCallback);
        Configs.Hotkeys.COPY.getKeybind().setCallback(keybindCallback);
    }

    public static class ChangeCallback implements IValueChangeCallback<ConfigBoolean> {
        @Override
        public void onValueChanged(ConfigBoolean config) {
            Screen gui = mc_.currentScreen;
        }
    }
}
