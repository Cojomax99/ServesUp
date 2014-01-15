package net.cojo.servesup.blocks;

import net.cojo.servesup.court.CourtBuilder;
import net.cojo.servesup.gui.GuiBuildCourt;
import net.cojo.servesup.tileentity.TileEntityGameManager;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;

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
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float localVecX, float localVecY, float localVecZ) {
		if (world.isRemote)
			return true;

		TileEntityGameManager manager = (TileEntityGameManager)world.getBlockTileEntity(x, y, z);

		if (!manager.isCourtBuilt()) {
			AxisAlignedBB aabb = CourtBuilder.buildCourt(world, x, y, z, manager.getOrientation());

			manager.setCourtData(aabb);
		} else {
			System.out.println("HOIERUIOWE");
			manager.startGame();
		}

		return false;
	}

	/**
	 * Called when the block is clicked by a player. Args: x, y, z, entityPlayer
	 */
	@Override
	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
		if (!world.isRemote) {
			TileEntityGameManager manager = (TileEntityGameManager)world.getBlockTileEntity(x, y, z);

			manager.rotate();
			System.out.println(manager.getOrientation());
			
			FMLCommonHandler.instance().showGuiScreen(new GuiBuildCourt(manager.getOrientation()));
		}
	}

}
