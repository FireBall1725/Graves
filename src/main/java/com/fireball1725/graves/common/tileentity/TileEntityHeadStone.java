package com.fireball1725.graves.common.tileentity;

public class TileEntityHeadStone extends TileEntityBase {
    public TileEntityHeadStone() {
        super();
    }

    @Override
    public String getCustomName() {
        return hasCustomName() ? this.customName : "";
    }
}
