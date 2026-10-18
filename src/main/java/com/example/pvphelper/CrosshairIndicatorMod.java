package com.example.pvphelper;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class CrosshairIndicatorMod implements ClientModInitializer {

    private static final float MIN_FALL_DISTANCE = 1.5f;
    private static final int OFFSET_Y = 12;

    private static final int COLOR_AXE = 0xFF5555;
    private static final int COLOR_MACE = 0x55FF55;

    private static final String TEXT_AXE = "DUNG RIU (PHA KHIEN)";
    private static final String TEXT_MACE = "DUNG MACE (SMASH)";

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> render(drawContext));
    }

    private void render(DrawContext drawContext) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;

        if (player == null || client.world == null) {
            return;
        }
        if (client.options.hudHidden) {
            return;
        }

        // Chi hien thi khi dang roi tu do
        if (player.fallDistance <= MIN_FALL_DISTANCE) {
            return;
        }

        // Tam ngam phai chi vao mot thuc the
        HitResult hit = client.crosshairTarget;
        if (!(hit instanceof EntityHitResult entityHit)) {
            return;
        }

        Entity target = entityHit.getEntity();
        if (!(target instanceof PlayerEntity otherPlayer)) {
            return;
        }

        String message;
        int color;

        if (otherPlayer.isBlocking()) {
            message = TEXT_AXE;
            color = COLOR_AXE;
        } else {
            message = TEXT_MACE;
            color = COLOR_MACE;
        }

        TextRenderer textRenderer = client.textRenderer;
        Text text = Text.literal(message);

        int screenWidth = drawContext.getScaledWindowWidth();
        int screenHeight = drawContext.getScaledWindowHeight();

        int x = (screenWidth - textRenderer.getWidth(text)) / 2;
        int y = screenHeight / 2 + OFFSET_Y;

        // shadow = true
        drawContext.drawText(textRenderer, text, x, y, color, true);
    }
}
