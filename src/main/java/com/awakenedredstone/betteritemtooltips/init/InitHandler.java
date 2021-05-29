package com.awakenedredstone.betteritemtooltips.init;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import com.awakenedredstone.betteritemtooltips.config.Callbacks;
import com.awakenedredstone.betteritemtooltips.config.Configs;
import com.awakenedredstone.betteritemtooltips.config.InputHandler;
import com.awakenedredstone.betteritemtooltips.util.ScrollUtil;
import net.minecraft.client.MinecraftClient;

public class InitHandler implements IInitializationHandler {

    @Override
    public void registerModHandlers() {
        ConfigManager.getInstance().registerConfigHandler("betteritemtooltips", new Configs());

        InputEventHandler.getKeybindManager().registerKeybindProvider(InputHandler.getInstance());
        InputEventHandler.getInputManager().registerMouseInputHandler(ScrollUtil.getInstance());
        Callbacks.init(MinecraftClient.getInstance());
    }
}
