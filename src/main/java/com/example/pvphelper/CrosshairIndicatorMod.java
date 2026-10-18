package com.example.pvphelper;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class CrosshairIndicatorMod implements ClientModInitializer {

    private static final double MIN_FALL_DISTANCE = 1.5;

    // Khoang cach tu tam ngam len mep duoi cua chu (don vi: pixel)
    // Muon chu cao hon nua thi tang so nay (vi du 20, 30)
    private static final int OFFSET_ABOVE = 12;

    // Chieu cao mot dong chu cua Minecraft
    private static final int TEXT_HEIGHT = 9;

    // 0xFF5555 va 0x55FF55 them kenh alpha FF o dau (ARGB) de chac chan hien thi
    private static final int COLOR_AXE = 0xFFFF5555;
    private static final int COLOR_MACE = 0xFF55FF55;

    private static final String TEXT_AXE = "DUNG RIU (PHA KHIEN)";
    private static final String TEXT_MACE = "DUNG MACE (SMASH)";

    @Override
    public void onInitializeClient() {
        HudElementRegistry.attachElementAfter(
                VanillaHudElements.CROSSHAIR,
                Identifier.fromNamespaceAndPath("pvphelper", "crosshair_indicator"),
                (graphics, deltaTracker) -> render(graphics)
        );
    }

    private void render(GuiGraphics graphics) {
        Minecraft client = Minecraft.getInstance();
        LocalPlayer player = client.player;

        if (player == null || client.level == null) {
            return;
        }
        if (client.options.hideGui) {
            return;
        }

        // Chi hien thi khi dang roi tu do
        if (player.fallDistance <= MIN_FALL_DISTANCE) {
            return;
        }

        // Tam ngam phai chi vao mot thuc the
        HitResult hit = client.hitResult;
        if (!(hit instanceof EntityHitResult entityHit)) {
            return;
        }

        Entity target = entityHit.getEntity();
        if (!(target instanceof Player otherPlayer)) {
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

        Font font = client.font;

        // Can giua man hinh theo chieu ngang
        int x = (graphics.guiWidth() - font.width(message)) / 2;

        // Nam tren tam ngam: lay tam man hinh tru khoang cach va tru chieu cao chu
        int y = graphics.guiHeight() / 2 - OFFSET_ABOVE - TEXT_HEIGHT;

        // shadow = true
        graphics.drawString(font, message, x, y, color, true);
    }
}
