package net.cojo.servesup.blocks;

import net.cojo.servesup.court.CourtBuilder;
import net.cojo.servesup.tileentity.TileEntityGameManager;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockGameManager extends BlockContainer {

	public BlockGameManager(int id) {
		super(id, Material.redstoneLight);
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityGameManager();
	}
	
    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
	@Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4) {
        super.onBlockAdded(par1World, par2, par3, par4);   
    }
	
    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        if (world.isRemote)
        	return true;
    	
    	TileEntityGameManager manager = (TileEntityGameManager)world.getBlockTileEntity(x, y, z);
        
        CourtBuilder.buildCourt(world, x, y, z, manager.getOrientation());
    	
    	return false;
    }
    
    /**
     * Called when the block is clicked by a player. Args: x, y, z, entityPlayer
     */
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
    	if (!world.isRemote) {
    		TileEntityGameManager manager = (TileEntityGameManager)world.getBlockTileEntity(x, y, z);
    		
    		manager.rotate();
    		System.out.println(manager.getOrientation());
    	}
    }

}
