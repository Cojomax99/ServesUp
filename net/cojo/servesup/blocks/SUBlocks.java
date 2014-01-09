package net.cojo.servesup.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.cojo.servesup.tileentity.TileEntityGameManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;

public class SUBlocks {
	
	public static Block net;
	public static Block gameManager;
	
	public static void init() {
		net = (new BlockNet(3000, "iron_bars", "iron_bars", Material.iron, true)).setHardness(5.0F).setResistance(10.0F).setUnlocalizedName("net");
		gameManager = (new BlockGameManager(3001)).setHardness(10F).setUnlocalizedName("game_manager");
		
		GameRegistry.registerBlock(net, "Net");
		LanguageRegistry.addName(net, "Vball net");
		
		GameRegistry.registerBlock(gameManager, "GameManager");
		LanguageRegistry.addName(gameManager, "Game Manager");
		
		GameRegistry.registerTileEntity(TileEntityGameManager.class, "GameManager");
	}
}
