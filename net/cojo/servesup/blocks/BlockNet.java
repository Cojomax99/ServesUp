package net.cojo.servesup.blocks;

import net.cojo.servesup.ModInfo;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockNet extends BlockPane {

	public BlockNet(int id, String par2Str, String par3Str, Material par4Material, boolean par5) {
		super(id, par2Str, par3Str, par4Material, par5);
	}

/*	@SideOnly(Side.CLIENT)

	*//**
	 * When this method is called, your block should register all the icons it needs with the given IconRegister. This
	 * is the only chance you get to register icons.
	 *//*
	public void registerIcons(IconRegister ir) {
		this.blockIcon = ir.registerIcon(ModInfo.ICONLOCATION + "net");
	}*/
	
	@Override
    public boolean canPaneConnectTo(IBlockAccess access, int x, int y, int z, ForgeDirection dir)
    {
        return canThisPaneConnectToThisBlockID(access.getBlockId(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ)) 
        		|| access.isBlockSolidOnSide(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ, dir.getOpposite(), false)
        		|| access.getBlockId(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) == Block.fence.blockID;
    }

}
