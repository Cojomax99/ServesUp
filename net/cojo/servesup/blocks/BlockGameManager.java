package net.cojo.servesup.blocks;

import net.cojo.servesup.tileentity.TileEntityGameManager;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockGameManager extends BlockContainer {

	public BlockGameManager(int id, Material material) {
		super(id, material);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityGameManager();
	}

}
