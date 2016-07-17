package com.fireball1725.graves.client.gui;

import com.fireball1725.graves.Graves;
import com.fireball1725.graves.common.block.BlockGrave;
import com.fireball1725.graves.common.block.Blocks;
import com.fireball1725.graves.common.entity.capabilities.GraveCapability;
import com.fireball1725.graves.common.entity.capabilities.IGraveCapability;
import com.fireball1725.graves.common.network.packets.PacketStartup;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;

import java.awt.*;
import java.io.IOException;

public class GuiStartUp extends GuiScreen {
    private GuiButton close;
    private IBakedModel model;

    @Override
    public void initGui() {
        super.initGui();
        ScaledResolution sr = new ScaledResolution(mc);
        buttonList.add(close = new GuiButton(buttonList.size(), sr.getScaledWidth() / 2 - 50, height - 100, 100, 20, I18n.translateToLocal("button.close")));

        model = mc.getBlockRendererDispatcher().getModelForState(Blocks.BLOCK_GRAVE.block.getDefaultState().withProperty(BlockGrave.WORLDGEN, true));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution sr = new ScaledResolution(mc);
        String s = I18n.translateToLocal("gui.startup.text");

        int width = mc.fontRendererObj.getStringWidth(s);
        mc.fontRendererObj.drawString(s, (sr.getScaledWidth() - width) / 2, 130, Color.white.hashCode());

        if (model != null) {
            GlStateManager.pushMatrix();
            GlStateManager.disableCull();
            GlStateManager.enableDepth();
            GlStateManager.pushMatrix();
            GlStateManager.translate(sr.getScaledWidth() / 2 - 35, 100, 0);
            GlStateManager.rotate(155, 1, 0, 0);
            GlStateManager.rotate(45, 0, 1, 0);
            GlStateManager.scale(50, 50, 50);
            mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            mc.getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightnessColor(model, 1, 0, 1, 1);
            GlStateManager.popMatrix();
            GlStateManager.disableDepth();
            GlStateManager.popMatrix();
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button == close)
            mc.thePlayer.closeScreen();
    }

    @Override
    public void onGuiClosed() {
        EntityPlayer player = mc.thePlayer;
        if (player.hasCapability(GraveCapability.GRAVE_CAPABILITY, null)) {
            IGraveCapability grave = player.getCapability(GraveCapability.GRAVE_CAPABILITY, null);
            grave.setSeenStartUp();
        }
        Graves.packetHandler.sendToServer(new PacketStartup());
        super.onGuiClosed();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
