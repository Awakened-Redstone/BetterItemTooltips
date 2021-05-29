package com.awakenedredstone.betteritemtooltips;

import com.awakenedredstone.betteritemtooltips.init.InitHandler;
import com.awakenedredstone.betteritemtooltips.tooltip.TooltipMain;
import fi.dy.masa.malilib.event.InitializationHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

import java.awt.*;

public class BetterItemTooltips implements ModInitializer {

    public static boolean disabled = false;

    @Override
    public void onInitialize() {
        InitializationHandler.getInstance().registerInitializationHandler(new InitHandler());
        ItemTooltipCallback.EVENT.register(TooltipMain::modifyTooltip);
    }
}
