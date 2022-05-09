package io.github.darkkronicle.darkkore.util;

import com.mojang.blaze3d.systems.RenderSystem;
import lombok.experimental.UtilityClass;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

@UtilityClass
public class RenderUtil {

    public void drawOutline(MatrixStack matrices, int x, int y, int width, int height, int color) {
        fillOutline(matrices, x, y, x + width, y + height, color);
    }

    public void fillOutline(MatrixStack matrices, int x, int y, int x2, int y2, int color) {
        // Top line
        fill(matrices, x, y, x2, y + 1, color);
        // Left line
        fill(matrices, x, y + 1, x + 1, y2 - 1, color);
        // Right line
        fill(matrices, x2 - 1, y + 1, x2, y2 - 1, color);
        // Bottom line
        fill(matrices, x, y2 - 1, x2, y2, color);
    }

    public void drawVerticalLine(MatrixStack matrices, int x, int y, int height, int color) {
        drawRectangle(matrices, x, y, 1, height, color);
    }

    public void drawHorizontalLine(MatrixStack matrices, int x, int y, int width, int color) {
        drawRectangle(matrices, x, y, width, 1, color);
    }

    public void drawRectangle(MatrixStack matrices, int x, int y, int width, int height, int color) {
        fill(matrices, x, y, x + width, y + height, color);
    }

    public static void fill(MatrixStack matrices, int x1, int y1, int x2, int y2, int color) {
        fill(matrices.peek().getPositionMatrix(), x1, y1, x2, y2, color);
    }

    public static void fill(Matrix4f matrix, int x1, int y1, int x2, int y2, int color) {
        int i;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }
        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;
        }
        float a = (float)(color >> 24 & 0xFF) / 255.0f;
        float r = (float)(color >> 16 & 0xFF) / 255.0f;
        float g = (float)(color >> 8 & 0xFF) / 255.0f;
        float b = (float)(color & 0xFF) / 255.0f;
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(matrix, x1, y2, 0.0f).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, x2, y2, 0.0f).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, x2, y1, 0.0f).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, x1, y1, 0.0f).color(r, g, b, a).next();
        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }


    public static void fillGradient(Matrix4f matrix, BufferBuilder builder, int startX, int startY, int endX, int endY, int colorStart, int colorEnd) {
        float a = (float)(colorStart >> 24 & 0xFF) / 255.0f;
        float r = (float)(colorStart >> 16 & 0xFF) / 255.0f;
        float g = (float)(colorStart >> 8 & 0xFF) / 255.0f;
        float b = (float)(colorStart & 0xFF) / 255.0f;
        float a2 = (float)(colorEnd >> 24 & 0xFF) / 255.0f;
        float r2 = (float)(colorEnd >> 16 & 0xFF) / 255.0f;
        float g2 = (float)(colorEnd >> 8 & 0xFF) / 255.0f;
        float b2 = (float)(colorEnd & 0xFF) / 255.0f;
        builder.vertex(matrix, endX, startY, 0).color(r, g, b, a).next();
        builder.vertex(matrix, startX, startY, 0).color(r, g, b, a).next();
        builder.vertex(matrix, startX, endY, 0).color(r2, g2, b2, a2).next();
        builder.vertex(matrix, endX, endY, 0).color(r2, g2, b2, a2).next();
    }

    public void fillGradient(MatrixStack matrices, int startX, int startY, int endX, int endY, int colorStart, int colorEnd) {
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        fillGradient(matrices.peek().getPositionMatrix(), bufferBuilder, startX, startY, endX, endY, colorStart, colorEnd);
        tessellator.draw();
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
    }

}