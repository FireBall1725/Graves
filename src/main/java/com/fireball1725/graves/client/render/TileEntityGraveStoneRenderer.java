package com.fireball1725.graves.client.render;

import com.fireball1725.graves.tileentity.TileEntityGraveStone;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;

public class TileEntityGraveStoneRenderer extends TileEntityBaseRenderer {
    public static TileEntityGraveStoneRenderer _instance = null;

    public static TileEntityGraveStoneRenderer instance() {
        if (_instance == null)
            _instance = new TileEntityGraveStoneRenderer();
        return _instance;
    }


    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage) {
        if (te instanceof TileEntityGraveStone) {
            this.saveBoundTexture();

            int[][] savedGLState = modifyGLState(new int[]{GL11.GL_BLEND, GL11.GL_LIGHTING}, null);
            TileEntityGraveStone graveStone = (TileEntityGraveStone) te;

            this.renderTextOnBlock(graveStone.getPlayerName(), EnumFacing.NORTH, EnumFacing.NORTH, x, y, z, 2.5F, 128.0F, 35.0F, 0.0F, 0);

            this.restoreGlState(savedGLState);
            this.loadBoundTexture();
        }
    }
}
