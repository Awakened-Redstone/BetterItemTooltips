package com.awakenedredstone.betteritemtooltips.mixin;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HandledScreen.class)
public interface IHandledScreenMixin {
    @Accessor("focusedSlot") Slot getHoveredSlot();
}
