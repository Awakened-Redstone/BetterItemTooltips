package com.awakenedredstone.betteritemtooltips.mixin;

import com.awakenedredstone.betteritemtooltips.config.Configs;
import com.awakenedredstone.betteritemtooltips.tooltip.TooltipMain;
import com.awakenedredstone.betteritemtooltips.util.TooltipMode;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector4f;
import net.minecraft.text.OrderedText;
import net.minecraft.util.math.Matrix4f;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

import static com.awakenedredstone.betteritemtooltips.BetterItemTooltips.disabled;

@Mixin(Screen.class)
public abstract class ScreenMixin extends AbstractParentElement {

    @Shadow
    protected TextRenderer textRenderer;
    @Shadow
    public int width;
    @Shadow
    public int height;
    @Shadow
    @Nullable
    protected MinecraftClient client;

    @Shadow
    public abstract void renderOrderedTooltip(MatrixStack matrices, List<? extends OrderedText> lines, int x, int y);

    @Redirect(method = "renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/List;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;renderOrderedTooltip(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/List;II)V"))
    public void renderTooltip(Screen screen, MatrixStack matrices, List<? extends OrderedText> lines, int x, int y) {
        if (disabled) {
            this.renderOrderedTooltip(matrices, lines, x, y);
            return;
        }
        switch ((TooltipMode) Configs.Settings.TOOLTIP_MODE.getOptionListValue()) {
            case TEXT_SCROLL:
                renderTrimmedOrderedTooltip(matrices, lines, x, y);
                break;
            case TOOLTIP_SCROLL:
                renderMovableOrderedTooltip(matrices, lines, x, y);
        }
    }

    @SuppressWarnings("deprecation")
    public void renderMovableOrderedTooltip(MatrixStack matrices, List<? extends OrderedText> lines, int x, int y) {
        if (!lines.isEmpty()) {
            int tooltipMaxWidth = 0;

            for (OrderedText orderedText : lines) {
                int j = this.textRenderer.getWidth(orderedText);
                if (j > tooltipMaxWidth) {
                    tooltipMaxWidth = j;
                }
            }

            int xStart = x + 12;
            int yStart = y - 12;
            int tooltipMaxHeight = 8;
            if (lines.size() > 1) {
                tooltipMaxHeight += 2 + (lines.size() - 1) * 10;
            }

            if (xStart + tooltipMaxWidth > this.width && x > this.width / 2) {
                xStart -= 28 + tooltipMaxWidth;
            }

            if (yStart + tooltipMaxHeight + 6 > this.height) {
                yStart = this.height - tooltipMaxHeight - 6;
            }

            if (yStart < 12) yStart = 12;

            xStart += TooltipMain.horizontalScroll;
            yStart += TooltipMain.verticalScroll;

            matrices.push();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();
            bufferBuilder.begin(7, VertexFormats.POSITION_COLOR);
            Matrix4f matrix4f = matrices.peek().getModel();
            fillGradient(matrix4f, bufferBuilder, xStart - 3, yStart - 4, xStart + tooltipMaxWidth + 3, yStart - 3, 400, -267386864, -267386864);
            fillGradient(matrix4f, bufferBuilder, xStart - 3, yStart + tooltipMaxHeight + 3, xStart + tooltipMaxWidth + 3, yStart + tooltipMaxHeight + 4, 400, -267386864, -267386864);
            fillGradient(matrix4f, bufferBuilder, xStart - 3, yStart - 3, xStart + tooltipMaxWidth + 3, yStart + tooltipMaxHeight + 3, 400, -267386864, -267386864);
            fillGradient(matrix4f, bufferBuilder, xStart - 4, yStart - 3, xStart - 3, yStart + tooltipMaxHeight + 3, 400, -267386864, -267386864);
            fillGradient(matrix4f, bufferBuilder, xStart + tooltipMaxWidth + 3, yStart - 3, xStart + tooltipMaxWidth + 4, yStart + tooltipMaxHeight + 3, 400, -267386864, -267386864);
            fillGradient(matrix4f, bufferBuilder, xStart - 3, yStart - 3 + 1, xStart - 3 + 1, yStart + tooltipMaxHeight + 3 - 1, 400, 1347420415, 1344798847);
            fillGradient(matrix4f, bufferBuilder, xStart + tooltipMaxWidth + 2, yStart - 3 + 1, xStart + tooltipMaxWidth + 3, yStart + tooltipMaxHeight + 3 - 1, 400, 1347420415, 1344798847);
            fillGradient(matrix4f, bufferBuilder, xStart - 3, yStart - 3, xStart + tooltipMaxWidth + 3, yStart - 3 + 1, 400, 1347420415, 1347420415);
            fillGradient(matrix4f, bufferBuilder, xStart - 3, yStart + tooltipMaxHeight + 2, xStart + tooltipMaxWidth + 3, yStart + tooltipMaxHeight + 3, 400, 1344798847, 1344798847);
            RenderSystem.enableDepthTest();
            RenderSystem.disableTexture();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.shadeModel(7425);
            bufferBuilder.end();
            BufferRenderer.draw(bufferBuilder);
            RenderSystem.shadeModel(7424);
            RenderSystem.disableBlend();
            RenderSystem.enableTexture();
            VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
            matrices.translate(0.0D, 0.0D, 400.0D);

            for (int s = 0; s < lines.size(); ++s) {
                OrderedText orderedText2 = lines.get(s);
                if (orderedText2 != null) {
                    this.textRenderer.draw(orderedText2, (float) xStart, (float) yStart, -1, true, matrix4f, immediate, false, 0, 15728880);
                }

                boolean showNbt = Configs.Settings.ALWAYS_SHOW.getBooleanValue() || Configs.Hotkeys.SHOW.getKeybind().isKeybindHeld();
                if (s == 0 && !(Configs.Hotkeys.SHOW_ONLY_NBT.getKeybind().isKeybindHeld() && showNbt)) {
                    yStart += 2;
                }

                yStart += 10;
            }
            immediate.draw();
            matrices.pop();
        }
    }

    @SuppressWarnings("deprecation")
    public void renderTrimmedOrderedTooltip(MatrixStack matrices, List<? extends OrderedText> lines, int x, int y) {
        if (!lines.isEmpty()) {
            int tooltipMaxWidth = 0;

            for (OrderedText orderedText : lines) {
                int j = this.textRenderer.getWidth(orderedText);
                if (j > tooltipMaxWidth) {
                    tooltipMaxWidth = j;
                }
            }
            int maxWidth = Configs.Settings.MAX_WIDTH.getIntegerValue();
            TooltipMain.horizontalScrollMax = Math.max(tooltipMaxWidth - maxWidth, 0);
            if (tooltipMaxWidth > maxWidth) {
                tooltipMaxWidth = maxWidth;
            }

            int xStart = x + 12;
            int yStart = y - 12;
            int tooltipMaxHeight = 8;
            if (lines.size() > 1) {
                tooltipMaxHeight += 2 + (lines.size() - 1) * 10;
            }

            if (xStart + tooltipMaxWidth > this.width && x > this.width / 2) {
                xStart -= 28 + tooltipMaxWidth;
            }

            if (yStart + tooltipMaxHeight + 6 > this.height) {
                yStart = this.height - tooltipMaxHeight - 6;
            }

            if (yStart < 12) yStart = 12;

            matrices.push();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();
            bufferBuilder.begin(7, VertexFormats.POSITION_COLOR);
            Matrix4f matrix4f = matrices.peek().getModel();
            fillGradient(matrix4f, bufferBuilder, xStart - 3, yStart - 4, xStart + tooltipMaxWidth + 3, yStart - 3, 400, -267386864, -267386864);
            fillGradient(matrix4f, bufferBuilder, xStart - 3, yStart + tooltipMaxHeight + 3, xStart + tooltipMaxWidth + 3, yStart + tooltipMaxHeight + 4, 400, -267386864, -267386864);
            fillGradient(matrix4f, bufferBuilder, xStart - 3, yStart - 3, xStart + tooltipMaxWidth + 3, yStart + tooltipMaxHeight + 3, 400, -267386864, -267386864);
            fillGradient(matrix4f, bufferBuilder, xStart - 4, yStart - 3, xStart - 3, yStart + tooltipMaxHeight + 3, 400, -267386864, -267386864);
            fillGradient(matrix4f, bufferBuilder, xStart + tooltipMaxWidth + 3, yStart - 3, xStart + tooltipMaxWidth + 4, yStart + tooltipMaxHeight + 3, 400, -267386864, -267386864);
            fillGradient(matrix4f, bufferBuilder, xStart - 3, yStart - 3 + 1, xStart - 3 + 1, yStart + tooltipMaxHeight + 3 - 1, 400, 1347420415, 1344798847);
            fillGradient(matrix4f, bufferBuilder, xStart + tooltipMaxWidth + 2, yStart - 3 + 1, xStart + tooltipMaxWidth + 3, yStart + tooltipMaxHeight + 3 - 1, 400, 1347420415, 1344798847);
            fillGradient(matrix4f, bufferBuilder, xStart - 3, yStart - 3, xStart + tooltipMaxWidth + 3, yStart - 3 + 1, 400, 1347420415, 1347420415);
            fillGradient(matrix4f, bufferBuilder, xStart - 3, yStart + tooltipMaxHeight + 2, xStart + tooltipMaxWidth + 3, yStart + tooltipMaxHeight + 3, 400, 1344798847, 1344798847);
            RenderSystem.enableDepthTest();
            RenderSystem.disableTexture();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.shadeModel(7425);
            bufferBuilder.end();
            BufferRenderer.draw(bufferBuilder);
            RenderSystem.shadeModel(7424);
            RenderSystem.disableBlend();
            RenderSystem.enableTexture();
            VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
            matrices.translate(0.0D, 0.0D, 400.0D);

            Vector4f v4f = new Vector4f(0, 0, 0, 1);
            v4f.transform(matrices.peek().getModel());
            if (this.client != null) {
                int scale = this.client.getWindow().calculateScaleFactor(this.client.options.guiScale, this.client.forcesUnicodeFont());
                RenderSystem.enableScissor(xStart * scale, 0, tooltipMaxWidth * scale, height * scale + 64);
            }
            for (int s = 0; s < lines.size(); ++s) {
                OrderedText orderedText2 = lines.get(s);
                if (orderedText2 != null) {
                    this.textRenderer.draw(orderedText2, (float) xStart - (TooltipMain.horizontalScroll), (float) yStart, -1, true, matrix4f, immediate, false, 0, 15728880);
                }

                boolean showNbt = Configs.Settings.ALWAYS_SHOW.getBooleanValue() || Configs.Hotkeys.SHOW.getKeybind().isKeybindHeld();
                if (s == 0 && TooltipMain.verticalScroll == 0 && !(Configs.Hotkeys.SHOW_ONLY_NBT.getKeybind().isKeybindHeld() && showNbt)) {
                    yStart += 2;
                }

                yStart += 10;
            }
            immediate.draw();
            RenderSystem.disableScissor();
            matrices.pop();
        }
    }
}
